package com.bee.open.ant.fast.composeopen.ui.start

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
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.FBAD
import com.bee.open.ant.fast.composeopen.load.FBADUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.net.GetServiceData.getVpnNetData
import com.bee.open.ant.fast.composeopen.net.IpUtils
import com.bee.open.ant.fast.composeopen.ui.end.ResultActivity
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    var job: Job? = null
    var job2: Job? = null
    var isJump = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumVpnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    init()
                    StartImage()
                }
            }
            checkAD()
            CountdownTimerExample()
        }
        onBackPressedDispatcher.addCallback {

        }
        lifecycleScope.launch(Dispatchers.IO) {
            CanDataUtils.postSessionData()
        }
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
        BaseAdLoad.startOpenBOIBOIUBU.preload(this)
        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
        BaseAdLoad.mainNativeHome.preload(this)
    }


    private fun updateUserOpinions() {
        if (DataKeyUtils.userAdType) {
            return
        }
        val debugSettings =
            ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("BCD99A19DFC84C1B71AF2A884D73059C")
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
                    }
                }
            },
            {
                DataKeyUtils.userAdType = true
            }
        )
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        job = null
    }

    private fun init() {
        updateUserOpinions()
        lifecycleScope.launch(Dispatchers.IO) {
            getVpnNetData()
            IpUtils.getIfConfig()
            IpUtils.getOnlyIp()
        }
    }

    @Composable
    fun CountdownTimerExample() {
        var progress by remember { mutableStateOf(1f) }
        val totalTime = 12_000L
        val interval = 100L
        var startTimer by remember { mutableStateOf(true) }

        LaunchedEffect(key1 = startTimer) {
            object : CountDownTimer(totalTime, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    progress = 1 - (millisUntilFinished / totalTime.toFloat())
                    if (progress > 0.1f && DataKeyUtils.userAdType) {
                        BaseAdLoad.showOpenAdIfCan(this@StartActivity, {
                            cancel()
                        }, {
                            startActivity(Intent(this@StartActivity, MainActivity::class.java))
                            finish()
                            isJump= true
                        })
                    }
                }

                override fun onFinish() {
                    if (!App.isBackDataQuan && DataKeyUtils.userAdType) {
                        progress = 1f
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                        finish()
                    } else {
                        progress = 1f
                        startTimer = false // Stop the current timer
                        startTimer = true  // Restart the timer
                    }
                }
            }.start()
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
