package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntAdLoad(private val context: Context, private val item: EveryADBean) :
    SoWhatCanYouDo(item) {

    private var ad: Any? = null
    private val adRequest: AdRequest get() = AdRequest.Builder().build()

    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        val type = when (item.adYype) {
            "plai" -> "Open"
            else -> "Inter"
        }
        loadInavnisduabnviosbvaoisubvd(onAdLoaded, onAdLoadFailed)
    }

    override fun showMyNameIsHei(
        activity: Activity,
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

        fun showAdMobFullScreenAd() {
            when (val adF = ad) {
                is InterstitialAd -> {
                    adF.run {
                        fullScreenContentCallback = callback
                        show(activity)
                    }
                }

                else -> onAdDismissed.invoke()
            }
        }

        showAdMobFullScreenAd()
    }


    private fun loadInavnisduabnviosbvaoisubvd(
        onAdLoaded: () -> Unit,
        onAdLoadFailed: (msg: String?) -> Unit
    ) {
        InterstitialAd.load(context,
            item.adIdKKKK,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    ad = interstitialAd
                    onAdLoaded.invoke()
//                    interstitialAd.setOnPaidEventListener { adValue ->
//                        WallNetDataUtils.getAdList(
//                            PaperThreeApp.instance,
//                            adValue,
//                            interstitialAd.responseInfo,
//                            item
//                        )
//                        BIBIUBADDDDUtils.putPointAdOnline(adValue.valueMicros)
//                        val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
//                        adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
//                        adRevenue.setAdRevenueNetwork(interstitialAd.responseInfo.mediationAdapterClassName)
//                        Adjust.trackAdRevenue(adRevenue)
//                    }
                }

                override fun onAdFailedToLoad(e: LoadAdError) = onAdLoadFailed.invoke(e.message)
            })
    }


}