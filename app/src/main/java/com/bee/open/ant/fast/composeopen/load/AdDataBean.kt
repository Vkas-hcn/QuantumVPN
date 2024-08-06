package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
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
        activity: ComponentActivity,
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
    var qtv_load_ip: String = "",
    var qtv_load_city: String = "",
    var qtv_show_ip: String = "",
    var qtv_show_city: String = ""
)

enum class ADType(val placeName: String) {
    FULL_One("openopenDjklasheoivs"),
    INNNNNNNN_1("int1111111asfeoipwjfsvksdlv"),
    INNNNNNNN_2("int222222sasderfgvyujwersd"),
    INNNNNNNN_RE("int3333yyyyyttttiiiiinnnt"),

    NNNAAAVVV_HHH("nnnaaavvvHhhjguvhendgkhmbndxdhnfdffgfd"),
    NNNAAAVVV_EEE("nnnaadldoencjghekssdkefavnrxzddgggfds"),
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

    @SerializedName("basex")
    val interRe: MutableList<EveryADBean>?,

    @SerializedName("saxc")
    val nnnhh: MutableList<EveryADBean>?,

    @SerializedName("mstan")
    val nnnee: MutableList<EveryADBean>?,
)

@Keep
data class AdOpenBean(
    val qua8: String? = "2",
    val hang: String? = "2",
    val miak: String? = "1",
    val ssfd:String? = ""
)