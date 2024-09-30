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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GuideAdLoad(private val context: Context, private var item: EveryADBean) :
    SoWhatCanYouDo(item) {

    private var ad: Any? = null
    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
//        if (App.isVpnState == 2 && item.qtv_load_ip != DataKeyUtils.tba_vpn_ip) {
//            Log.e(
//                "TAG",
//                "不相同ip禁止展示=${item.where}==${item.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}"
//            )
//            BaseAdLoad.getStartOpenAdData().clearAdCache()
//            BaseAdLoad.getStartOpenAdData().preload(context)
//            onAdDismissed.invoke()
//            return
//        }
        loadAppOpen(onAdLoaded, onAdLoadFailed)
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
                            while (Lifecycle.State.RESUMED != baseAct.lifecycle.currentState)
                                delay(200L)
                            onAdDismissed.invoke()
                        }
                    } else onAdDismissed.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.e(
                        "TAG",
                        "ad-where=${item.where},InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA}  Show success"
                    )
                    BaseAdLoad.countAD()
                }

                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    val baseAct = activity as? AppCompatActivity
                    if (null != baseAct) {
                        baseAct.lifecycleScope.launch {
                            while (Lifecycle.State.RESUMED != baseAct.lifecycle.currentState)
                                delay(200L)
                            onAdDismissed.invoke()
                        }
                    } else onAdDismissed.invoke()
                }

                override fun onAdClicked() {
                    Log.e("TAG", "onAdClicked: open_start")
                    BaseAdLoad.countAD(s = false, c = true)
                }
            }
        }

        fun showAdMobFullScreenAd() {
            if (App.isVpnState == 2 && item.qtv_load_ip != DataKeyUtils.tba_vpn_ip) {
                Log.e(
                    "TAG",
                    "不相同ip禁止展示=${item.where}==${item.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}"
                )
                BaseAdLoad.getStartOpenAdData().clearAdCache()
                BaseAdLoad.getStartOpenAdData().preload(activity)
                onAdDismissed.invoke()
                return
            }
            when (val adF = ad) {
                is AppOpenAd -> {
                    adF.run {
                        fullScreenContentCallback = callback
                        show(activity)
                        item = CanDataUtils.afterLoadQTV(item)
                    }
                }

                else -> onAdDismissed.invoke()
            }
        }
        showAdMobFullScreenAd()
    }

    private fun loadAppOpen(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        Log.e(
            "TAG",
            "ad-where=${item.where},InterstitialAd-id: ${item.adIdKKKK}, weight: ${item.adWeightHAHHA}  start preload"
        )
        item = CanDataUtils.beforeLoadQTV(item)
        CanDataUtils.antur14(item)
        AppOpenAd.load(
            context,
            item.adIdKKKK,
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    ad = appOpenAd
                    onAdLoaded.invoke()
                    CanDataUtils.antur15(adBean)
                    appOpenAd.setOnPaidEventListener { adValue ->
                        adValue.let {
                            CanDataUtils.postAdAllData(
                                it,
                                appOpenAd.responseInfo,
                                item
                            )
                            CanDataUtils.toPointAdQTV(adValue, appOpenAd.responseInfo)
                        }
                    }
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-success"
                    )
                }

                override fun onAdFailedToLoad(e: LoadAdError) {
                    CanDataUtils.antur17(item, e.message)
                    onAdLoadFailed.invoke(e.message)
                    Log.e(
                        "TAG",
                        "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-error=${e.message}"
                    )
                }
            })
    }


}