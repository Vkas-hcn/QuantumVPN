package com.bee.open.ant.fast.composeopen.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils.complexLogicReturnsFalse
import com.bee.open.ant.fast.composeopen.net.ClockUtils.ifAddThis
import com.bee.open.ant.fast.composeopen.net.GetNetDataUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class App : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: App
        fun getVpnInstance(): App {
            return instance
        }

        lateinit var saveUtils: MMKV

        var isVpnState = 0
        var isShow by mutableStateOf(true)

    }

    private var appBackgroundTimestamp: Long = 0

    override fun onCreate() {
        super.onCreate()
        instance = this
        ifAddThis("com.bee.open.ant.fast.composeopen.app.App") {
            MMKV.initialize(this)
            saveUtils =
                MMKV.mmkvWithID("saveUtils", MMKV.MULTI_PROCESS_MODE)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            if (DataKeyUtils.uuid_open.isEmpty() && !complexLogicReturnsFalse(
                    listOf(3, 4),
                    "false"
                )
            ) {
                ifAddThis("com.bee.open.ant.fast.composeopen.app.App") {
                    DataKeyUtils.uuid_open = UUID.randomUUID().toString()
                }
            }
            getBlackList(this)
        }
    }

    private fun getBlackList(context: Context) {
        if (DataKeyUtils.black_data.isNotEmpty() && !complexLogicReturnsFalse(
            listOf(3, 4),
            "false"
        )) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            GetNetDataUtils.getMapData(
                DataKeyUtils.blackUrl,
                ClockUtils.cloakMapData(context),
                onNext = {
                    Log.e("TAG", "The blacklist request is successful：$it")
                    DataKeyUtils.black_data = it
                },
                onError = {
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(10000)
                        Log.e("TAG", "The blacklist request failed：$it")
                        getBlackList(context)
                    }
                })
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        if ((System.currentTimeMillis() - appBackgroundTimestamp) >= 3000) {
            restartApp()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopState() {
        appBackgroundTimestamp = System.currentTimeMillis()

    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}