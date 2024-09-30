package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NativeAdLoadDis(private val context: Context, private var item2: EveryADBean) :
    SoWhatCanYouDo(item2) {
    companion object {
        var nativeAdHome: NativeAd? = null
        var nativeAdEnd: NativeAd? = null
    }

    //        var item2 :EveryADBean?=null
    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {

//        if (item2?.where == "saxc") {
//            item2 = BaseAdLoad.getMainNativeAdData().getAdDataBean()
//        } else {
//            item2 = BaseAdLoad.getEndNativeAdData().getAdDataBean()
//        }

        Log.e(
            "TAG",
            "ad-where=${item2?.where}, InterstitialAd-id: ${item2?.adIdKKKK}, weight: ${item2?.adWeightHAHHA}  start preload==App.isVpnState == 2${App.isVpnState == 2}"
        )
        var ip = ""

        CanDataUtils.antur14(item2!!)
        AdLoader.Builder(context, item2!!.adIdKKKK).apply {
            forNativeAd { ad ->
                item2 = item2?.let { CanDataUtils.beforeLoadQTV(it) }!!
                Log.e("TAG", "loadHowAreYou=qtv_load_ip=非服务器: ${item2.qtv_load_ip}")
                if (item2?.where == "saxc") {
                    nativeAdHome = ad
                } else {
                    nativeAdEnd = ad
                }
                CanDataUtils.antur15(adBean)
                ad.setOnPaidEventListener { adValue ->
                    Log.e("TAG", "原生广告 -${item2?.where}，开始上报-非服务器: ${item2.qtv_load_ip}")
                    CanDataUtils.postAdAllData(
                        adValue,
                        ad.responseInfo,
                        item2!!
                    )
                    CanDataUtils.toPointAdQTV(adValue, ad.responseInfo)
                }
                onAdLoaded.invoke()
            }
            withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(e: LoadAdError) {
                    Log.e(
                        "TAG",
                        "ad-where-${item2?.where}, id :${item2?.adIdKKKK}, adweight: ${item2?.adWeightHAHHA} onAdLoaded-error=${e.message}"
                    )
                    onAdLoadFailed.invoke(e.message)
                    if (item2?.where == "saxc") {
                        nativeAdHome = null
                    } else {
                        nativeAdEnd = null
                    }
                    CanDataUtils.antur17(item2!!, e.message)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.e(
                        "TAG",
                        "ad-where-${item2?.where}, id :${item2?.adIdKKKK}, adweight: ${item2?.adWeightHAHHA} onAdLoaded-success"
                    )
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
        if (item2?.where == "saxc") {
            if (nativeAdHome == null) return

        } else {
            if (nativeAdEnd == null) return
        }
        // 不相同ip禁止加载
        if (App.isVpnState == 2 && item2?.qtv_load_ip != DataKeyUtils.tba_vpn_ip) {
            Log.e(
                "TAG",
                "不相同ip禁止展示=${item2?.where}==${item2?.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}"
            )
            // 清除缓存
            val adLoader =
                if (item2?.where == "saxc") BaseAdLoad.getMainNativeAdData() else BaseAdLoad.getEndNativeAdData()
            adLoader.clearAdCache()
            // 处理广告显示逻辑
            // 添加重新加载广告的逻辑
            loadHowAreYou({
                showMyNameIsHei(activity, nativeParent, onAdDismissed)
            }, { msg ->
                Log.e("TAG", "重新加载广告失败：$msg")
                BaseAdLoad.setActivityShowIntAd(activity, false)
                onAdDismissed.invoke()
            })

            return
        }




        if (BaseAdLoad.canShowAD()) {
            onAdDismissed.invoke()
        }
        item2 = item2?.let { CanDataUtils.afterLoadQTV(it) }!!
        BaseAdLoad.countAD(true)
    }

    private val adRequest: AdRequest get() = AdRequest.Builder().build()

}
