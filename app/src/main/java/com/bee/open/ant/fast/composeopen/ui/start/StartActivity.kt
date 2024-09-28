package com.bee.open.ant.fast.composeopen.ui.start

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.content.Intent
import android.net.VpnService
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.app.App.Companion.adjustNum
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad.isActivityResumed
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.load.FBAD
import com.bee.open.ant.fast.composeopen.load.FBADUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.net.GetServiceData.getVpnNetData
import com.bee.open.ant.fast.composeopen.net.IpUtils
import com.bee.open.ant.fast.composeopen.ui.end.ResultActivity
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    var job: Job? = null
    var job2: Job? = null
    private var startTimer = mutableStateOf(true)
    lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumVpnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    StartImage()
                }
            }
            checkAD()
            CountdownTimerExample(startTimer.value) {
                startTimer.value = false // Reset the timer when finished
            }
        }
        onBackPressedDispatcher.addCallback {

        }
        updateUserOpinions()
        FBAD.showVpnPermission(this@StartActivity){
            checkVpnPermission()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            initAdJust()
            getVpnNetData()
            IpUtils.getIfConfig()
            IpUtils.getOnlyIp()
            CanDataUtils.postSessionData()
        }
        requestPermissionForResultVPN =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                requestPermissionForResult(it)
            }
    }

    private fun checkVpnPermission() {
        if (!checkVPNPermission(this)) {
            VpnService.prepare(this)?.let(requestPermissionForResultVPN::launch)
        }
    }

    private fun requestPermissionForResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {

        }
    }

    private fun checkVPNPermission(ac: Activity): Boolean {
        VpnService.prepare(ac).let {
            return it == null
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkAD() {
        if (FBADUtils.isAppInit) {
            FBADUtils.isAppInit = false
            job2 = GetServiceData.countDown(8, 500, MainScope(), {
                FBAD.checkADData {
                    if (it) {
                        preLoadAD()
                        job2?.cancel()
                    }
                }
            }, {
                FBAD.checkADDataWithServer()
                preLoadAD()
            })
        } else {
            preLoadAD()
        }
    }

    private fun preLoadAD() {
        BaseAdLoad.getMainNativeAdData().preload(this)
        if(!DataKeyUtils.firstDialogState2){return}
        BaseAdLoad.getStartOpenAdData().preload(this)
    }

    @SuppressLint("HardwareIds")
    private fun initAdJust() {

        val timeStart = System.currentTimeMillis()
        Adjust.addSessionCallbackParameter(
            "customer_user_id",
            Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
        )
        val appToken = "ih2pm2dr3k74"
        val environment: String = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(application, appToken, environment)
        config.needsCost = true
        config.setOnAttributionChangedListener { attribution ->
            Log.e("TAG", "adjust=${attribution}")
            adjustNum++
            val timeEnd = (System.currentTimeMillis() - timeStart) / 1000
            CanDataUtils.postPointData("llo", "time", timeEnd)
            val bh = attribution.network.contains(
                "organic",
                true
            ).not()
            if (!DataKeyUtils.ad_j_v && attribution.network.isNotEmpty() && bh) {
                DataKeyUtils.ad_j_v = true
            }
            val op1 = if (!bh) {
                "o"
            } else if (DataKeyUtils.ad_j_v) {
                "m"
            } else {
                "null"
            }
            CanDataUtils.postPointData("iit", "op1", op1, "op2", adjustNum)
        }
        Adjust.onCreate(config)
    }

    private fun updateUserOpinions() {
        if (DataKeyUtils.userAdType) {
            return
        }
        val debugSettings =
            ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("202C0DAA36EB5148BDEA8A1E6E36A4B6")
                .build()
        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()
        val consentInformation: ConsentInformation =
            UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params, {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) {
                    if (consentInformation.canRequestAds()) {
                        DataKeyUtils.userAdType = true
                        startTimer()
                    }
                }
            },
            {
                DataKeyUtils.userAdType = true
                startTimer()
            }
        )
    }

    private fun startTimer() {
        startTimer.value = true
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        job = null
    }


    @Composable
    fun CountdownTimerExample(startTimer: Boolean, onTimerFinish: () -> Unit) {
        var progress by remember { mutableFloatStateOf(1f) }
        val totalTime = 12_000L
        val interval = 100L

        LaunchedEffect(key1 = startTimer) {
            if (startTimer) {
                object : CountDownTimer(totalTime, interval) {
                    override fun onTick(millisUntilFinished: Long) {
                        progress = 1 - (millisUntilFinished / totalTime.toFloat())
                        if (progress > 0.1f && DataKeyUtils.userAdType) {
                            BaseAdLoad.showOpenAdIfCan(this@StartActivity, {
                                cancel()
                            }, {
                                progress = 1f
                                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                                finish()
                            })
                        }
                    }

                    override fun onFinish() {
                        progress = 1f
                        onTimerFinish()
                        if (!App.isBackDataQuan && DataKeyUtils.userAdType) {
                            progress = 1f
                            startActivity(Intent(this@StartActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }.start()
            }
        }

        ProgressBarDemo(progress = progress)
    }

}


@Composable
fun StartImage() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = com.bee.open.ant.fast.composeopen.R.drawable.bg_start),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(127.dp))
        Image(
            painter = painterResource(id = com.bee.open.ant.fast.composeopen.R.drawable.ic_start_logo),
            contentDescription = null,
            modifier = Modifier.requiredSize(82.dp)
        )
    }

}


@Composable
fun CustomProgressBar(progress: Float) {
    val backgroundColor = Color(0xFFECECEC)
    val progressColor = Color(0xFF1CA27A)

    Box(
        contentAlignment = Alignment.BottomCenter, // 居中对齐在底部
        modifier = Modifier
            .fillMaxSize() // 填充父容器大小
            .padding(bottom = 30.dp) // 底部边距30dp
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .height(4.dp) // 高度为4dp
                .width(320.dp), // 宽度为320dp
            backgroundColor = backgroundColor,
            color = progressColor
        )
    }
}

@Composable
fun ProgressBarDemo(progress: Float) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        CustomProgressBar(progress = progress)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuantumVpnTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            StartImage()
        }
    }
}
