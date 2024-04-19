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
    @SerializedName("keep")
    var adYype: String? = null,

    @SerializedName("book")
    var adNetOperator: String,

    @SerializedName("tom")
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
    @SerializedName("nunn")
    val showMax: Int = 0,
    @SerializedName("comping")
    val clickMax: Int = 0,
    @SerializedName("tuop")
    val start: MutableList<EveryADBean>?,

    @SerializedName("intu")
    val inter: MutableList<EveryADBean>?,

    @SerializedName("tintuba")
    val inter2: MutableList<EveryADBean>?,
)

@Keep
data class AdOpenBean(
    val hang: String?= "2",
    val miak: String?= "1",
)