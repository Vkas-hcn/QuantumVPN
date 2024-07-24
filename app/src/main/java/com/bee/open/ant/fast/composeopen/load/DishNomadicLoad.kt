package com.bee.open.ant.fast.composeopen.load

import android.content.Context
import android.util.Log
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData.getLocalOpenData


class DishNomadicLoad(
    private val contextcaniscnaiesbc: Context,
    private val snvkdjnv: ADType,
    private val svdkalc: MutableList<EveryADBean>,
    private val nslcasidckn: MutableList<SoWhatCanYouDo>,
    private val canusdkbcaushdconLoad: (Boolean) -> Unit = {}
) {
    companion object {
        fun showAdBlacklist(): Boolean {
            val blackData = DataKeyUtils.black_data != "scorpion"
            val result = when (getLocalOpenData().hang) {
                "1" -> {
                    !blackData
                }

                "2" -> {
                    true
                }

                else -> {
                    false
                }
            }
            if (result && !DataKeyUtils.black_data_up_type) {
                CanDataUtils.postPointData("antur1")
                DataKeyUtils.black_data_up_type = true
            }
            return result
        }

        fun getSpoilerData() {
            DataKeyUtils.spoiler_data = when (getLocalOpenData().miak) {
                "1" -> {
                    true
                }

                "2" -> {
                    false
                }

                "3" -> {
                    DataKeyUtils.black_data == "scorpion"
                }

                else -> {
                    false
                }
            }
            Log.e("TAG", "brand-1: ${DataKeyUtils.spoiler_data}")
        }
    }

    fun startLoadBUH() {
        if (svdkalc.isEmpty()) canusdkbcaushdconLoad.invoke(false) else preloadAdByIndexibscdaiuhsdbcuahsbcukashbcu(
            0
        )
    }


    private fun preloadAdByIndexibscdaiuhsdbcuahsbcukashbcu(index: Int) {
        val item = svdkalc.getOrNull(index)
        if (null == item) {
            Log.e("TAG", "quan AD-${item?.where}- item null")
            canusdkbcaushdconLoad.invoke(false)
            return
        }

        if ((!showAdBlacklist()) && (item.adYype == "ity" || (item.where == "hhhhnn"))) {
            Log.e("TAG", "黑名单屏蔽${item.where}广告加载 ")
            canusdkbcaushdconLoad.invoke(false)
            return
        }
        val baseAdanscinc = when (item.adYype) {
            "plai" -> GuideAdLoad(context = contextcaniscnaiesbc, item = item)
            "nnnee" -> NativeAdLoad(context = contextcaniscnaiesbc, item = item)
            "ity" -> IntAdLoad(context = contextcaniscnaiesbc, item = item)
            else -> null
        }
        if (null == baseAdanscinc) {
            Log.e("TAG", "WallPaper AD baseAd null  ${item.adYype}")
            preloadAdByIndexibscdaiuhsdbcuahsbcukashbcu(index + 1)
            return
        }
        baseAdanscinc.loadHowAreYou(onAdLoaded = {
            nslcasidckn.add(baseAdanscinc)
            nslcasidckn.sortByDescending { it.adBean.adWeightHAHHA }
            canusdkbcaushdconLoad.invoke(true)
            Log.e(
                "TAG",
                "ad-where-${item.where}, id: ${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-success"
            )
        }, onAdLoadFailed = {
            Log.e(
                "TAG",
                "ad-where-${item.where}, id :${item.adIdKKKK}, adweight: ${item.adWeightHAHHA} onAdLoaded-error=${it}"
            )
            preloadAdByIndexibscdaiuhsdbcuahsbcukashbcu(index + 1)
        })
    }

}