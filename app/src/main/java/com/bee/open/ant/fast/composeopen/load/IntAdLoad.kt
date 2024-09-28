package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.ui.end.ResultActivity
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntAdLoad(private val context: Context, private var item: EveryADBean) :
    SoWhatCanYouDo(item) {

    private var ad: Any? = null
    private val adRequest: AdRequest get() = AdRequest.Builder().build()

    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        val type = when (item.adYype) {
            "plai" -> "Open"
            else -> "Inter"
        }
        Log.e(
            "TAG",
            "ad-where=${item.where},InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA}  start preload"
        )
        loadInavnisduabnviosbvaoisubvd(onAdLoaded, onAdLoadFailed)
    }

    override fun showMyNameIsHei(
        activity: ComponentActivity,
        nativeParent: ViewGroup?,
        onAdDismissed: () -> Unit
    ) {
        val callback: FullScreenContentCallback by lazy {
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    val baseAct = activity as? AppCompatActivity
                    if (null != baseAct) {
                        baseAct.lifecycleScope.launch {
                            while (Lifecycle.State.RESUMED != baseAct.lifecycle.currentState) delay(
                                200L
                            )
                            onAdDismissed.invoke()
                        }
                    } else onAdDismissed.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.e(
                        "TAG",
                        "ad-where=${item.where},InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA}  Show"
                    )
                    BaseAdLoad.countAD(true)
                }

                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    Log.e(
                        "TAG",
                        "ad-where=${item.where},InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA} Failed Show"
                    )
                    val baseAct = activity as? AppCompatActivity
                    if (null != baseAct) {
                        baseAct.lifecycleScope.launch {
                            while (Lifecycle.State.RESUMED != baseAct.lifecycle.currentState) delay(
                                200L
                            )
                            onAdDismissed.invoke()
                        }
                    } else onAdDismissed.invoke()
                }

                override fun onAdClicked() {
                    Log.e("TAG", "onAdClicked: Int")
                    BaseAdLoad.countAD(s = false, c = true)
                }
            }
        }

        fun showAdMobFullScreenAd(activity: ComponentActivity) {

            if (App.isVpnState == 2 && item.qtv_load_ip!= DataKeyUtils.tba_vpn_ip) {
                Log.e("TAG", "不相同ip禁止展示=${item.where}==${item.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}")
                // 添加重新加载广告的逻辑
                loadInavnisduabnviosbvaoisubvd({
                    // 广告加载成功后再次尝试展示广告
                    showAdMobFullScreenAd(activity)
                }, { msg ->
                    Log.e("TAG", "重新加载广告失败：$msg")
                    onAdDismissed.invoke()
                })
                return
            }
            when (val adF = ad) {
                is InterstitialAd -> {
                    adF.run {
                        fullScreenContentCallback = callback
                        show(activity)
                        item = CanDataUtils.afterLoadQTV(item)
                    }
                }

                else -> onAdDismissed.invoke()
            }
        }

        showAdMobFullScreenAd(activity)
    }


    private fun loadInavnisduabnviosbvaoisubvd(
        onAdLoaded: () -> Unit,
        onAdLoadFailed: (msg: String?) -> Unit
    ) {
        item = CanDataUtils.beforeLoadQTV(item)
        CanDataUtils.antur14(item)
        InterstitialAd.load(context,
            item.adIdKKKK,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-success"
                    )
                    ad = interstitialAd
                    CanDataUtils.antur15(adBean)
                    onAdLoaded.invoke()
                    interstitialAd.setOnPaidEventListener { adValue ->
                        CanDataUtils.postAdAllData(
                            adValue,
                            interstitialAd.responseInfo,
                            item
                        )
                        CanDataUtils.toPointAdQTV(adValue, interstitialAd.responseInfo)
                    }
                }

                override fun onAdFailedToLoad(e: LoadAdError){
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-error=${e.message}"
                    )
                    CanDataUtils.antur17(item,e.message)
                    onAdLoadFailed.invoke(e.message)
                }
            })
    }


}