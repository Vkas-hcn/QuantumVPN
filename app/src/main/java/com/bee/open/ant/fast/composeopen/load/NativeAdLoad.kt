package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NativeAdLoad(private val context: Context, private var item: EveryADBean) :
    SoWhatCanYouDo(item) {

    private var nativeAd: NativeAd? = null

    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        Log.e(
            "WallPaper AD=${item.where}",
            " NativeAd, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} start preload"
        )
        item = CanDataUtils.beforeLoadQTV(item)
        CanDataUtils.antur14(item)
        AdLoader.Builder(context, item.adIdKKKK).apply {
            forNativeAd { ad ->
                Log.e("TAG", "原生广告 -${item.where}，加载成功: ")
                nativeAd = ad
                CanDataUtils.antur15(adBean)
                ad.setOnPaidEventListener { adValue ->
                    Log.e("TAG", "原生广告 -${item.where}，开始上报: ")
                    CanDataUtils.postAdAllData(
                        adValue,
                        ad.responseInfo,
                        item
                    )
                    CanDataUtils.toPointAdQTV(adValue, ad.responseInfo)
                }
                onAdLoaded.invoke()
            }
            withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(e: LoadAdError) {
                    Log.e("TAG", "原生广告 -${item.where}，加载失败: ")
                    onAdLoadFailed.invoke(e.message)
                    if (item.where == "saxc") {
                        App.appNativeAdHome = null
                    } else {
                        App.appNativeAdEnd = null
                    }
                    CanDataUtils.antur17(item, e.message)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.e("TAG", "onAdClicked: Nat")
                    BaseAdLoad.countAD(s = false, c = true)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    BaseAdLoad.countAD(s = false, c = true)
                }
            })
            withNativeAdOptions(NativeAdOptions.Builder().apply {
                setAdChoicesPlacement(NativeAdOptions.ADCHOICES_BOTTOM_RIGHT)
            }.build())

        }.build().loadAd(adRequest)
    }

    override fun showMyNameIsHei(
        activity: ComponentActivity,
        nativeParent: ViewGroup?,
        onAdDismissed: () -> Unit
    ) {
        if (null == nativeAd) return
        Log.e(
            "TAG",
            " NativeAd, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} show"
        )
        if (item.where == "saxc") {
            App.appNativeAdHome = null
        } else {
            App.appNativeAdEnd = null
        }
        if (BaseAdLoad.canShowAD()) {
            if (item.where == "saxc") {
                App.appNativeAdHome = nativeAd
            } else {
                App.appNativeAdEnd = nativeAd
            }
            nativeAd = null
        }
        item = CanDataUtils.afterLoadQTV(item)
        BaseAdLoad.countAD(true)
    }

    private val adRequest: AdRequest get() = AdRequest.Builder().build()

    fun getNaiveAdData(): NativeAd? {
        return nativeAd
    }

}