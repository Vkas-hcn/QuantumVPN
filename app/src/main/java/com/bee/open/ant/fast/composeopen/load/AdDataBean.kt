package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


abstract class SoWhatCanYouDo(
    val adBean: EveryADBean,
    var loadTime: Long = System.currentTimeMillis()
) {

    abstract fun loadHowAreYou(
        onAdLoaded: () -> Unit = {},
        onAdLoadFailed: (msg: String?) -> Unit = {}
    )

    abstract fun showMyNameIsHei(
        activity: Activity,
        nativeParent: ViewGroup? = null,
        onAdDismissed: () -> Unit = {}
    )
}

@Keep
data class EveryADBean(
    @SerializedName("ad_type")
    var adYype: String? = null,

    @SerializedName("ad_from")
    var adNetOperator: String,

    @SerializedName("ad_id")
    var adIdKKKK: String,

    @SerializedName("fa_c_oute")
    var adWeightHAHHA: Int = 0,

    var where: String? = null,

    val adCacheInvalidTime: Int = 2,
)

enum class ADType(val placeName: String) {
    FULL_One("openopenDjklasheoivs"),
    INNNNNNNN_1("int1111111asfeoipwjfsvksdlv"),
    INNNNNNNN_2("int222222sasderfgvyujwersd"),
}

@Keep
data class AdvertiseEntity(
    @SerializedName("showNum")
    val showMax: Int = 0,
    @SerializedName("clickNum")
    val clickMax: Int = 0,
    @SerializedName("open_start")
    val start: MutableList<EveryADBean>?,

    @SerializedName("open_connect")
    val inter: MutableList<EveryADBean>?,

    @SerializedName("open_back")
    val inter2: MutableList<EveryADBean>?,
)

@Keep
data class AdOpenBean(
    val clockType: String?= "2",
    val raoliu: String?= "1",
)