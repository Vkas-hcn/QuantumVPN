package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NomadicLoad(private val snvlinjvk: ADType) {

    private val dataList: MutableList<EveryADBean> = mutableListOf()
    private val cacheListncsudbca: MutableList<SoWhatCanYouDo> = mutableListOf()
    private var isLoadBHBU = false
    private var vsnoevn: (Boolean) -> Unit = {}
    val haveCache: Boolean get() = cacheListncsudbca.isNotEmpty()

    fun adCaData(): SoWhatCanYouDo? = cacheListncsudbca.removeFirstOrNull()

    fun getAdDataBean(): EveryADBean? {
        return dataList.firstOrNull()
    }

    fun showFullScreenAdBIUYBUI(activity: Activity, onAdDismissed: () -> Unit) {
        if (cacheListncsudbca.isEmpty()) {
            onAdDismissed.invoke()
            return
        }
        val baseAd = adCaData()
        if (null == baseAd) {
            Log.e("TAG", "${baseAd?.adBean?.where}---无缓存: ")
            onAdDismissed.invoke()
            return
        }
        val activityType = when (activity) {
            is ComponentActivity -> activity
            is AppCompatActivity -> activity
            else -> {
                Log.e("AdManager", "Unsupported activity type: ${activity.javaClass.name}")
                onAdDismissed.invoke()
                return
            }
        }
        baseAd.showMyNameIsHei(activity = activityType, onAdDismissed = onAdDismissed)
        vsnoevn = {}
        if (baseAd.adBean.where == "saxc" || baseAd.adBean.where == "mstan") {
            preload(activity)
        }
    }

    fun initializeSource(data: MutableList<EveryADBean>?) {
        dataList.run {
            clear()
            addAll(data ?: mutableListOf())
            sortByDescending { it.adWeightHAHHA }
        }
    }

    fun preload(context: Context) {
        MainScope().launch {
            BaseAdLoad.initializeAdConfig(DataKeyUtils.adHistory)
            if (dataList.isEmpty()) return@launch
            if (!BaseAdLoad.canShowAD()) return@launch
            val loadIp = when (dataList[0].where) {
                "tuop" -> {
                    DataKeyUtils.online_load_ip_open
                }

                "intu" -> {
                    DataKeyUtils.online_load_ip_connect_int
                }

                "basex" -> {
                    DataKeyUtils.online_load_ip_end_int
                }

                "tintuba" -> {
                    DataKeyUtils.online_load_ip_service_int
                }

                "saxc" -> {
                    DataKeyUtils.online_load_ip_home_nav
                }

                "mstan" -> {
                    DataKeyUtils.online_load_ip_end_nav
                }

                else -> {
                    ""
                }
            }
            if (App.isVpnState == 2 && loadIp.isNotBlank() && loadIp != DataKeyUtils.tba_vpn_ip) {
                Log.e(
                    "TAG",
                    "缓存ip不相同，清除数据，重新加载=${dataList[0].where}==loadIp=${loadIp}---nowIp-${DataKeyUtils.tba_vpn_ip}"
                )
                clearAdCache()
                clearLoadIp()
                preload(context)
                return@launch
            }
            Log.e("TAG", "preload: haveCache=${cacheListncsudbca.size}")
            if (haveCache && isCacheOverTime().not()) {
                Log.e(
                    "TAG",
                    "广告-${dataList[0].where}-已有缓存: weight=${dataList[0].adWeightHAHHA}",
                )
                return@launch
            }
            if (haveCache) {
                Log.e(
                    "TAG",
                    "广告-${dataList[0].where}-已有缓存: weight=${dataList[0].adWeightHAHHA}",
                )
                return@launch
            }
            if (isLoadBHBU) return@launch
            isLoadBHBU = true
            DishNomadicLoad(context, snvlinjvk, dataList, cacheListncsudbca) {
                isLoadBHBU = false
                vsnoevn.invoke(it)
            }.startLoadBUH()
        }
    }

    private fun isCacheOverTime(): Boolean {
        val item = cacheListncsudbca.firstOrNull() ?: return false
        return if (System.currentTimeMillis() - item.loadTime >= (1000L * 60L * 60L)) {
            cacheListncsudbca.remove(item)
            true
        } else {
            false
        }
    }

    fun clearAdCache() {
        Log.e("TAG", "clearAdCache: ${dataList[0].where}")
        cacheListncsudbca.clear()
    }

    private fun clearLoadIp(){
        when (dataList[0].where) {
            "tuop" -> {
                DataKeyUtils.online_load_ip_open = ""
            }

            "intu" -> {
                DataKeyUtils.online_load_ip_connect_int = ""
            }

            "basex" -> {
                DataKeyUtils.online_load_ip_end_int = ""
            }

            "tintuba" -> {
                DataKeyUtils.online_load_ip_service_int = ""
            }

            "saxc" -> {
                DataKeyUtils.online_load_ip_home_nav = ""
            }

            "mstan" -> {
                DataKeyUtils.online_load_ip_end_nav = ""
            }
        }
    }
}