package com.bee.open.ant.fast.composeopen.load

import android.content.Context
import android.util.Log
import com.bee.open.ant.fast.composeopen.app.App
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
        var triggerCount = 0

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
                    !blackData
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
                    DataKeyUtils.black_data != "scorpion"
                }

                else -> {
                    false
                }
            }
            Log.e("TAG", "brand-1: ${DataKeyUtils.spoiler_data}")
        }

        fun getBuyingShieldData(): Boolean {
            return when (getLocalOpenData().qua8) {
                "1" -> {
                    false
                }

                "2" -> {
                    !DataKeyUtils.ad_j_v
                }

                "3" -> {
                    true
                }

                else -> {
                    false
                }
            }
        }

        fun getMainClickData(): Boolean {
            return when (getLocalOpenData().guide) {
                "1" -> {
                    DataKeyUtils.black_data == "scorpion"
                }

                "2" -> {
                    false
                }

                else -> {
                    DataKeyUtils.black_data == "scorpion"
                }
            }
        }

        // 获取间隔次数
        fun getIntervalTimes(): Boolean {
            val num = getLocalOpenData().kate?.toIntOrNull() ?: return false
            // 首次触发一定弹窗
            if (triggerCount == 0) {
                triggerCount++
                return true
            }
            // 处理 num 值的不同情况
            return when {
                num == 0 -> {
                    // num 为 0，表示每次触发都会弹窗
                    true
                }

                else -> {
                    // 当 num > 0 时，判断当前的触发次数是否应该弹窗
                    val cycle = num + 1 // 弹窗周期
                    val shouldShow = triggerCount % cycle == 0
                    triggerCount = (triggerCount + 1) % cycle // 计数器循环重置
                    shouldShow
                }
            }
        }

        fun parseTwoNumbers(): Pair<Int, Int> {
            // 默认值
            val default = 20
            val num = getLocalOpenData().fooey ?: ""
            // 分割字符串
            val parts = num.split("&")
            // 检查分割结果并尝试转换为数字，转换失败则使用默认值
            val firstNumber = parts.getOrNull(0)?.toIntOrNull() ?: default
            val secondNumber = parts.getOrNull(1)?.toIntOrNull() ?: default

            // 返回解析后的两个数字
            return Pair(firstNumber, secondNumber)
        }

        //获取是否自动连接
        fun getAutoConnectData(): Boolean {
            return when (getLocalOpenData().brisk) {
                "1" -> {
                    true
                }

                "2" -> {
                    false
                }

                "3" -> {
                    DataKeyUtils.ad_j_v
                }

                else -> {
                    DataKeyUtils.ad_j_v
                }
            }
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
        if (getBuyingShieldData() && (item.where == "saxc" || (item.where == "basex"))) {
            Log.e("TAG", "买量屏蔽${item.where}广告加载 ")
            canusdkbcaushdconLoad.invoke(false)
            return
        }
        if ((!showAdBlacklist()) && (item.adYype == "ity" || (item.where == "saxc"))) {
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

        }, onAdLoadFailed = {

            preloadAdByIndexibscdaiuhsdbcuahsbcukashbcu(index + 1)
        })
    }

}