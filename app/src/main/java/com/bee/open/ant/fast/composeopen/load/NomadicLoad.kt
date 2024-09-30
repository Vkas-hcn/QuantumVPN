package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class NomadicLoad(private val snvlinjvk: ADType) {

    private val dataList: MutableList<EveryADBean> = mutableListOf()
    private val cacheListncsudbca: MutableList<SoWhatCanYouDo> = mutableListOf()
    private var isLoadBHBU = false
    private var vsnoevn: (Boolean) -> Unit = {}
    val haveCache: Boolean get() = cacheListncsudbca.isNotEmpty()

     fun adCaData(): SoWhatCanYouDo? = cacheListncsudbca.removeFirstOrNull()

    fun showFullScreenAdBIUYBUI(activity: ComponentActivity, onAdDismissed: () -> Unit) {
        if (cacheListncsudbca.isEmpty()) {
            onAdDismissed.invoke()
            return
        }
        val baseAd = adCaData()
        if (null == baseAd) {
            onAdDismissed.invoke()
            return
        }
        baseAd.showMyNameIsHei(activity = activity, onAdDismissed = onAdDismissed)
        vsnoevn = {}
        if ( baseAd.adBean.where == "saxc"|| baseAd.adBean.where == "mstan") {
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
            if (haveCache && isCacheOverTime().not()) {
                Log.e("TAG", "广告-${dataList[0].where}-已有缓存: weight=${dataList[0].adWeightHAHHA}", )
                return@launch
            }
            if (haveCache) {
                Log.e("TAG", "广告-${dataList[0].where}-已有缓存: weight=${dataList[0].adWeightHAHHA}", )
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
        cacheListncsudbca.clear()
    }
}