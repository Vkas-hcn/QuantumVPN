package com.bee.open.ant.fast.composeopen.ui.end

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity
import com.bee.open.ant.fast.composeopen.ui.main.NativeAdHomeContent
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ResultActivity : ComponentActivity() {
    var showIntAd by mutableStateOf(false)
    var showSwitch by mutableStateOf(false)
    var jobDialog: Job? = null
    var adJobDialog: Job? = null
    var jobBackDialog: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumVpnTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1B6A56)
                ) {
                    titleView(this@ResultActivity)
                    SwitchDialog(this)
                    LoadingDialog(this)
                }
            }
        }
        onBackPressedDispatcher.addCallback {
            ClockUtils.ifAddThis("onBackPressedDispatcher") {}
            if (ClockUtils.complexLogicReturnsFalse(
                    listOf(2334, 2256), "onBackPressedDispatcher"
                )
            ) {
                return@addCallback
            }
            if (!ClockUtils.complexLogicAlwaysTrue("onBackPressedDispatcher")) {
                return@addCallback
            }
            if (showSwitch) {
                return@addCallback
            }
            backFun()
        }
        showNativeAd()
        showSwitchDialogFun()
        CanDataUtils.antur12()
    }

    private fun showSwitchDialogFun() {
        if (App.isVpnState == 2 && !App.showSwitchState && !DishNomadicLoad.getBuyingShieldData() && DishNomadicLoad.showAdBlacklist() && DishNomadicLoad.getIntervalTimes()) {
            showSwitch = true
            BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
            CanDataUtils.postPointData(
                "antur26",
                "qu",
                App.top_activity_Quan?.javaClass?.simpleName,
            )
        }
    }

    private fun showNativeAd() {
        adJobDialog?.cancel()
        adJobDialog = null
        val endNav = BaseAdLoad.getEndNativeAdData()
        endNav.preload(this)
        adJobDialog = lifecycleScope.launch {
            delay(300)
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                try {
                    while (true) {
                        if (endNav.haveCache) {
                            Log.e("TAG", "showNativeAd---: ${App.appNativeAdEnd == null}")
                            endNav.showFullScreenAdBIUYBUI(this@ResultActivity) {
                                adJobDialog?.cancel()
                                adJobDialog = null
                            }
                            break
                        }
                        delay(500)
                    }
                } catch (e: Exception) {
                    Log.e("ResultActivity", "Error showing native ad", e)
                }
            }
        }
    }

    fun backFun() {
        CanDataUtils.postPointData("antur21")
        showInt2Ad {
            finish()
        }
    }

    private fun showInt2Ad(nextFun: () -> Unit) {
        if (DishNomadicLoad.getBuyingShieldData() || !DishNomadicLoad.showAdBlacklist() || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }

        val inter = BaseAdLoad.getInterResultAdData()
        inter.preload(this)
        showIntAd = true
        jobBackDialog?.cancel()
        jobBackDialog = null

        val max = 5 * 10
        var adShown = false // 标志位，跟踪广告是否已显示

        jobBackDialog = GetServiceData.countDown(max, 100, MainScope(), { num ->
            if (inter.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED && !adShown) {
                Log.e("TAG", "showInt2Ad: 111111111")
                adShown = true // 设置标志位，广告已显示
                lifecycleScope.launch(Dispatchers.Main) {
                    showIntAd = false
                    inter.showFullScreenAdBIUYBUI(this@ResultActivity) {
                        nextFun()
                    }
                }
                false // 返回false以停止计时
            } else {
                true // 继续计时
            }
        }, {
            if (!adShown) { // 仅在广告未显示时执行
                Log.e("TAG", "showInt2Ad: 22222222")
                nextFun()
            }
        })
    }


    private fun showCloneDialogAd(seconds: Int = 3, nextFun: () -> Unit) {
        if ((!DishNomadicLoad.showAdBlacklist()) || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        jobDialog?.cancel()
        jobDialog = null
        val max = seconds * 10
        jobDialog = GetServiceData.countDown(max, 100, MainScope(), {
            if (it > 10) {
                BaseAdLoad.showDialogAdIfCan(this@ResultActivity) {
                    if (this.lifecycle.currentState == Lifecycle.State.RESUMED || this.lifecycle.currentState == Lifecycle.State.STARTED) {
                        nextFun()
                        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
                    }
                }
            }
        }, {
            nextFun()
        })
    }

    fun cloneDialogFun() {
        lifecycleScope.launch {
            CanDataUtils.postPointData(
                "antur28",
                "qu",
                App.top_activity_Quan?.javaClass?.simpleName,
            )
            showIntAd = true
            showCloneDialogAd {
                showIntAd = false
                showSwitch = false
            }
        }
    }
}


@Composable
fun titleView(activity: ResultActivity) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .requiredSize(24.dp)
                    .clickable {
                        activity.backFun()
                    },
            )
            Text(
                text = "Connect Report",
                color = Color.White,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding()
                    .weight(1f),
            )
            Spacer(Modifier.width(24.dp))
        }
        Text(
            text = if (App.isVpnState == 2) "Connection succeed" else "Disconnection succeed",
            color = Color.White,
            fontSize = 20.sp,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text = if (App.isVpnState == 2) "You are very safe right now!" else "You have exposed in danger!",
            color = Color(0xFFADE2C2),
            fontSize = 12.sp,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Image(
            painter = painterResource(id = if (App.isVpnState == 2) R.drawable.ic_connect else R.drawable.ic_disconnect),
            contentDescription = "Connect Success",
            modifier = Modifier
                .padding(top = 20.dp)
                .requiredSize(166.dp)
                .fillMaxWidth(),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            if (!BaseAdLoad.canShowAD()) {
                App.appNativeAdEnd == null
            }
            if (App.appNativeAdEnd != null) {
                NativeAdEndContent(App.appNativeAdEnd!!)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_ad_end_oc),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun NativeAdEndContent(nativeAd: NativeAd) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp),
        factory = { context ->
            val nativeAdView = NativeAdView(context)
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.layout_end, null) as NativeAdView
            nativeAdView.addView(adView)


            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.mediaView = adView.findViewById(R.id.ad_media)

            if (nativeAd.mediaContent != null) {
                adView.mediaView?.apply {
                    visibility = View.VISIBLE
                    mediaContent = nativeAd.mediaContent!!
                }
            } else {
                adView.mediaView?.visibility = View.GONE
            }
            adView.mediaView?.clipToOutline = true
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = nativeAd.callToAction
            }
            if (nativeAd.headline == null) {
                adView.headlineView?.visibility = View.INVISIBLE
            } else {
                adView.headlineView?.visibility = View.VISIBLE
                (adView.headlineView as TextView).text = nativeAd.headline
            }

            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon?.drawable
                )
                adView.iconView?.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
            nativeAdView


        }
    )
}

@Composable
fun LoadingDialog(activity: ResultActivity) {
    if (activity.showIntAd) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .background(Color(0xB3000000))) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = Color.Black)
                    Text(
                        text = "Ads will show soon",
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchDialog(activity: ResultActivity) {
    if (activity.showSwitch) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .background(Color(0xB3000000))) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(id = R.drawable.guanbi),
                            contentDescription = "Back",
                            alignment = Alignment.BottomEnd,
                            modifier = Modifier
                                .requiredSize(24.dp)
                                .clickable {
                                    activity.cloneDialogFun()
                                },
                        )
                    }
                    Text(
                        text = "Discover a Better Connection !",
                        color = Color.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "We've found a faster and more stable\n" +
                                "route for you.",
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    //Switch按钮控件
                    Box(
                        modifier = Modifier
                            .background(Color.Black, shape = RoundedCornerShape(12.dp))
                            .padding(top = 12.dp, bottom = 12.dp, start = 24.dp, end = 24.dp)
                            .clickable {
                                App.showSwitchState = true
                                activity.showSwitch = false
                                activity.finish()
                                CanDataUtils.postPointData(
                                    "antur27",
                                    "qu",
                                    App.top_activity_Quan?.javaClass?.simpleName,
                                )
                            },
                    ) {
                        Text(
                            text = "Switch Now",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuantumVpnTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF1B6A56)
        ) {
            titleView(activity = ResultActivity())
            LoadingDialog(activity = ResultActivity())
            SwitchDialog(activity = ResultActivity())
        }
    }
}

