package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
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
import com.bee.open.ant.fast.composeopen.ui.end.XmlResultActivity
import com.bee.open.ant.fast.composeopen.ui.main.XmlMainActivity
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NativeAdLoad(private val context: Context, private var item2: EveryADBean) :
    SoWhatCanYouDo(item2) {
    companion object {
        var nativeAdHome: NativeAd? = null
        var nativeAdEnd: NativeAd? = null
    }
    override fun loadHowAreYou(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        Log.e(
            "TAG",
            "ad-where=${item2.where}, NativeAdLoad-id: ${item2.adIdKKKK}, weight: ${item2.adWeightHAHHA}  start preload;is service=${App.isVpnState == 2}"
        )
        CanDataUtils.antur14(item2)
        AdLoader.Builder(context, item2.adIdKKKK).apply {
            forNativeAd { ad ->
                item2 = item2.let { CanDataUtils.beforeLoadQTV(it) }
                if (App.getVpnState()) {
                    when (item2.where) {
                        "saxc" -> {
                            DataKeyUtils.online_load_ip_home_nav = item2.qtv_load_ip
                        }
                        else -> {
                            DataKeyUtils.online_load_ip_end_nav = item2.qtv_load_ip
                        }
                    }
                }
                Log.e(
                    "TAG",
                    "ad-where=${item2.where}, NativeAdLoad-id: ${item2.adIdKKKK}, weight: ${item2.adWeightHAHHA}  load success"
                )
                Log.e("TAG", "loadHowAreYou=qtv_load_ip=服务器: ${item2.qtv_load_ip}")
                if (item2?.where == "saxc") {
                    nativeAdHome = ad
                } else {
                    nativeAdEnd = ad
                }
                CanDataUtils.antur15(adBean)
                ad.setOnPaidEventListener { adValue ->
                    Log.e("TAG", "原生广告 -${item2?.where}，开始上报-服务器: ${item2.qtv_load_ip}")
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
                    CanDataUtils.antur17(item2, e.message)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.e(
                        "TAG",
                        "ad-where-${item2.where}, id :${item2.adIdKKKK}, adweight: ${item2.adWeightHAHHA} onAdClicked-success"
                    )
                    BaseAdLoad.countAD(s = false, c = true)
                }
            })
            withNativeAdOptions(NativeAdOptions.Builder().apply {
                setAdChoicesPlacement(NativeAdOptions.ADCHOICES_BOTTOM_RIGHT)
            }.build())
        }.build().loadAd(adRequest)
    }

    override fun showMyNameIsHei(
        activity: Activity,
        nativeParent: ViewGroup?,
        onAdDismissed: () -> Unit
    ) {
        if (item2.where == "saxc") {
            if (nativeAdHome == null) return

        } else {
            if (nativeAdEnd == null) return
        }
        if (App.isVpnState == 2 && item2.qtv_load_ip != DataKeyUtils.tba_vpn_ip) {
            Log.e(
                "TAG",
                "不相同ip禁止展示=${item2.where}==${item2.qtv_load_ip}----${DataKeyUtils.tba_vpn_ip}"
            )
            return
        }
        if (item2.where == "saxc") {
            nativeAdHome?.let { setDisplayHomeNativeAdFlash(it, activity as XmlMainActivity) }
        } else {
            nativeAdEnd?.let { setDisplayEndNativeAdFlash(it, activity as XmlResultActivity) }
        }
    }

    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    private fun setDisplayHomeNativeAdFlash(ad: NativeAd, activity: XmlMainActivity) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            ad.let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED
                Log.e("TAG", "setDisplayHomeNativeAdFlash: ${state}")
                if (state) {
                    val adView = activity.layoutInflater.inflate(
                        R.layout.layout_main,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    BaseAdLoad.countAD(true)
                    Log.e(
                        "TAG",
                        "ad-where-${item2.where}, id :${item2.adIdKKKK}, adweight: ${item2.adWeightHAHHA} show-success"
                    )
                    val adLoader =
                        if (item2.where == "saxc") BaseAdLoad.getMainNativeAdData() else BaseAdLoad.getEndNativeAdData()
                    adLoader.clearAdCache()
                    nativeAdHome = null
                    item2 = item2.let { CanDataUtils.afterLoadQTV(it) }
                    CanDataUtils.antur30(item2)
                }
            }
        }
    }

    private fun setDisplayEndNativeAdFlash(ad: NativeAd, activity: XmlResultActivity) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            ad.let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED

                if (state) {
                    activity.binding.imgOc.isVisible = true

                    val adView = activity.layoutInflater.inflate(
                        R.layout.layout_end,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOc.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    BaseAdLoad.countAD(true)
                    Log.e(
                        "TAG",
                        "ad-where-${item2.where}, id :${item2.adIdKKKK}, adweight: ${item2.adWeightHAHHA} show-success"
                    )
                    val adLoader =
                        if (item2.where == "saxc") BaseAdLoad.getMainNativeAdData() else BaseAdLoad.getEndNativeAdData()
                    adLoader.clearAdCache()
                    nativeAdEnd = null
                    item2 = item2.let { CanDataUtils.afterLoadQTV(it) }
                    CanDataUtils.antur30(item2)
                }
            }
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        nativeAd.mediaContent?.let {
            adView.mediaView?.apply { setImageScaleType(ImageView.ScaleType.CENTER_CROP) }?.mediaContent =
                it
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
    }
}
