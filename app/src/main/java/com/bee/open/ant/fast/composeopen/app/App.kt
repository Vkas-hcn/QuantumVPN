package com.bee.open.ant.fast.composeopen.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.bee.open.ant.fast.composeopen.BuildConfig
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.load.FBADUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils.complexLogicReturnsFalse
import com.bee.open.ant.fast.composeopen.net.ClockUtils.ifAddThis
import com.bee.open.ant.fast.composeopen.net.GetNetDataUtils
import com.bee.open.ant.fast.composeopen.ui.start.StartActivity
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV
import de.blinkt.openvpn.api.ExternalOpenVPNService
import de.blinkt.openvpn.api.IOpenVPNAPIService
import de.blinkt.openvpn.api.IOpenVPNStatusCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class App : Application(), LifecycleObserver {
    var mService: IOpenVPNAPIService? = null

    companion object {
        lateinit var instance: App
        fun getVpnInstance(): App {
            return instance
        }

        lateinit var appContext: Context

        lateinit var saveUtils: MMKV
        var isVpnState = 0
        var isShow by mutableStateOf(true)
        var isBackDataQuan = false
//        var appNativeAdHome: NativeAd? by mutableStateOf(null)
//        var appNativeAdEnd: NativeAd? by mutableStateOf(null)
        var isAppRunning = false
        var ad_activity_Quan: Activity? = null
        var top_activity_Quan: Activity? = null
        var adjustNum = 0
        var showSwitchState = false
        var jumpSwitchState = false
        var connectSwitchState = false

    }

    var isBoot = false
    var whetherBackgroundSmild = false
    var flag = 0
    var job_Quan: Job? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this
        ifAddThis("com.bee.open.ant.fast.composeopen.app.App") {
            MMKV.initialize(this)
            Firebase.initialize(this)
            FirebaseApp.initializeApp(this)
            registerActivityLifecycleCallbacks(AppLifecycleTracker())
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            saveUtils =
                MMKV.mmkvWithID("saveUtils", MMKV.MULTI_PROCESS_MODE)
            if (DataKeyUtils.uuid_open.isEmpty() && !complexLogicReturnsFalse(
                    listOf(3, 4),
                    "false"
                )
            ) {
                ifAddThis("com.bee.open.ant.fast.composeopen.app.App") {
                    DataKeyUtils.uuid_open = UUID.randomUUID().toString()
                }
            }
            if (DataKeyUtils.tba_id_data.isEmpty() && !complexLogicReturnsFalse(
                    listOf(3, 4),
                    "false"
                )
            ) {
                DataKeyUtils.tba_id_data = UUID.randomUUID().toString()
            }

        }
        val appScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        if (isMainProcess(this)) {
            appScope.launch {
                haveRefData(this@App)
                getBlackList(this@App)
                FBADUtils.getFirebaseRemoteConfigData()
                FBADUtils.fourAppWait4SecondsToGetData()
                delay(4000)
                FBADUtils.appCircleToRequestFireData()
            }
            try {
                this.bindService(
                    Intent(this, ExternalOpenVPNService::class.java), mConnection, BIND_AUTO_CREATE
                )
            } catch (e: Exception) {
                Log.e("AppLifecycle", "bindService failed: $e")
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        try {
            this.unbindService(mConnection)
        } catch (e: Exception) {
        }
    }

    private fun getBlackList(context: Context) {
        if (DataKeyUtils.black_data.isNotEmpty() && !complexLogicReturnsFalse(
                listOf(3, 4),
                "false"
            )
        ) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("TAG", "Black--URL: ${DataKeyUtils.blackUrl}")
            GetNetDataUtils.getMapData(
                DataKeyUtils.blackUrl,
                ClockUtils.cloakMapData(context),
                onNext = {
                    Log.e("TAG", "The blacklist request is successful：$it")
                    DataKeyUtils.black_data = it
                },
                onError = {
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(5000)
                        Log.e("TAG", "The blacklist request failed：$it")
                        getBlackList(context)
                    }
                })
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        job_Quan?.cancel()
        job_Quan = null
        if (whetherBackgroundSmild && !isBackDataQuan) {
            isBoot = false
            whetherBackgroundSmild = false
            val intent = Intent(top_activity_Quan, StartActivity::class.java)
            top_activity_Quan?.startActivity(intent)
            isAppRunning = true
            DataKeyUtils.autoConnect=true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopState() {
        job_Quan = GlobalScope.launch {
            whetherBackgroundSmild = false
            delay(3000L)
            whetherBackgroundSmild = true
            ad_activity_Quan?.finish()
            if (top_activity_Quan is StartActivity) {
                top_activity_Quan?.finish()
            }
        }
    }

    private inner class AppLifecycleTracker : ActivityLifecycleCallbacks {

        override fun onActivityStarted(activity: Activity) {
            if (activity !is AdActivity) {
                top_activity_Quan = activity
            } else {
                ad_activity_Quan = activity
            }

            flag++
            isBackDataQuan = false
        }

        override fun onActivityStopped(activity: Activity) {
            flag--
            if (flag == 0) {
                isBackDataQuan = true
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity !is AdActivity) {
                top_activity_Quan = activity
            } else {
                ad_activity_Quan = activity
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (activity !is AdActivity) {
                top_activity_Quan = activity
            }
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity is AdActivity) {
                ad_activity_Quan = activity
            } else {
                top_activity_Quan = activity
            }
            Adjust.onPause()
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }

    private fun haveRefData(context: Context) {
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(p0: Int) {
                    when (p0) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            runCatching {
                                referrerClient?.installReferrer?.run {
                                    CanDataUtils.postInstallData(context, this)
                                }
                            }.exceptionOrNull()
                        }
                    }
                    referrerClient.endConnection()
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }.onFailure { e ->
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName?,
            service: IBinder?,
        ) {
            mService = IOpenVPNAPIService.Stub.asInterface(service)
            try {
                mService?.registerStatusCallback(mCallback)
            } catch (e: Exception) {
            }
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            mService = null
        }
    }
    private val mCallback = object : IOpenVPNStatusCallback.Stub() {
        override fun newStatus(uuid: String?, state: String?, message: String?, level: String?) {
            Log.e("TAG", "newStatus-App: ${state}")
            if (state == "CONNECTED") {
                isVpnState = 2
            }
            if (state == "NOPROCESS") {
                isVpnState = 0
            }
        }
    }

    private fun isMainProcess(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPid = Process.myPid()
        val packageName = context.packageName

        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        for (processInfo in runningAppProcesses) {
            if (processInfo.pid == myPid && processInfo.processName == packageName) {
                return true
            }
        }
        return false
    }
}