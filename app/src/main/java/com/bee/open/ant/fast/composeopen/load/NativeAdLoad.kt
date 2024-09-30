package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NativeAdLoad(private val context: Context, private var item: EveryADBean) :
    SoWhatCanYouDo(item) {
    companion object {
        var nativeAdHome: NativeAd? = null
        var nativeAdEnd: NativeAd? = null
    }

    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        Log.e(
            "TAG",
            "ad-where=${item.where}, InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA}  start preload"
        )
        item = CanDataUtils.beforeLoadQTV(item)
        CanDataUtils.antur14(item)
        AdLoader.Builder(context, item.adIdKKKK).apply {
            forNativeAd { ad ->
                Log.e(
                    "TAG",
                    "ad-where-${item.where}, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} forNativeAd-success"
                )
                if (item.where == "saxc") {
                    nativeAdHome = ad
                } else {
                    nativeAdEnd = ad
                }
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
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-success"
                    )
                }

                override fun onAdFailedToLoad(e: LoadAdError) {
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-error=${e.message}"
                    )
                    onAdLoadFailed.invoke(e.message)
                    if (item.where == "saxc") {
                       nativeAdHome= null
                    } else {
                        nativeAdEnd = null
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
        if (item.where == "saxc") {
            if (nativeAdHome == null) return

        } else {
            if (nativeAdEnd== null) return
        }
        // 不相同ip禁止加载
        if (App.isVpnState == 2 && item.qtv_load_ip != DataKeyUtils.tba_vpn_ip) {
            Log.e(
                "TAG",
                "不相同ip禁止展示=${item.where}==${item.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}"
            )
            // 添加重新加载广告的逻辑
            onAdDismissed.invoke()
            if (item.where == "saxc") {
                BaseAdLoad.getMainNativeAdData().clearAdCache()
                BaseAdLoad.getMainNativeAdData().preload(activity)
            } else {
                BaseAdLoad.getEndNativeAdData().clearAdCache()
                BaseAdLoad.getEndNativeAdData().preload(activity)
            }
            return
        }
//        if (item.where == "saxc") {
//            App.appNativeAdHome = null
//        } else {
//            App.appNativeAdEnd = null
//        }
        if (BaseAdLoad.canShowAD()) {
//            if (item.where == "saxc") {
//                App.appNativeAdHome = nativeAd
//            } else {
//                App.appNativeAdEnd = nativeAd
//            }
//            Log.e(
//                "TAG",
//                "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} show success"
//            )
//            nativeAd = null
            onAdDismissed.invoke()
        }
        item = CanDataUtils.afterLoadQTV(item)
        BaseAdLoad.countAD(true)
    }

    private val adRequest: AdRequest get() = AdRequest.Builder().build()

}
