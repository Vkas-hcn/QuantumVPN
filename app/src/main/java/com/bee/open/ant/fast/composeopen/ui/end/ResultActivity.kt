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
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity
import com.bee.open.ant.fast.composeopen.ui.main.NativeAdHomeContent
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ResultActivity : ComponentActivity() {
    var showIntAd by mutableStateOf(false)
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
            backFun()
        }
        showNaAd()
        CanDataUtils.antur12()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(300)
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && App.isAppRunning) {
                BaseAdLoad.mainNativeEnd.showFullScreenAdBIUYBUI(this@ResultActivity) {}
                App.isAppRunning = false
            }
        }
    }

    private fun showNaAd() {
        lifecycleScope.launch {
            while (isActive) {
                if (App.appNativeAdEnd == null) {
                    BaseAdLoad.mainNativeEnd.showFullScreenAdBIUYBUI(this@ResultActivity) {}
                } else {
                    cancel()
                }
                delay(500)
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
        if (!DishNomadicLoad.showAdBlacklist() || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        if (BaseAdLoad.interHaHaHaOPNNOPIN2.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
            lifecycleScope.launch(Dispatchers.Main) {
                showIntAd = true
                delay(1000)
                showIntAd = false
                BaseAdLoad.interHaHaHaOPNNOPIN2.showFullScreenAdBIUYBUI(this@ResultActivity) {
                    nextFun()
                }
            }
        } else
            nextFun()
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
            if(!BaseAdLoad.canShowAD()){
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
        }
    }
}

