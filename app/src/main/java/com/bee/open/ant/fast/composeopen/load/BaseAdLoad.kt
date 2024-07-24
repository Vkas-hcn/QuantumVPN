package com.bee.open.ant.fast.composeopen.load

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.data.NomadicFun
import com.bee.open.ant.fast.composeopen.data.NomadicFun.stringComplexLogicCheck
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.ui.end.ResultActivity
import com.bee.open.ant.fast.composeopen.ui.main.MainActivity
import com.bee.open.ant.fast.composeopen.ui.start.StartActivity

import com.facebook.appevents.AppEventsLogger
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale


object BaseAdLoad {

    var snlveijnaielv = 30
    var clickLimitBKUBOUIBI = 5
    val startOpenBOIBOIUBU = NomadicLoad(ADType.FULL_One)
    val interHaHaHaOPNNOPIN = NomadicLoad(ADType.INNNNNNNN_1)
    val interHaHaHaOPNNOPIN2 = NomadicLoad(ADType.INNNNNNNN_2)
    val mainNativeHome = NomadicLoad(ADType.NNNAAAVVV_HHH)
    val mainNativeEnd = NomadicLoad(ADType.NNNAAAVVV_EEE)
    fun initializeAdConfig(adConfigJson: String? = null) {
        var json = adConfigJson
        if (json.isNullOrBlank()) json = DataKeyUtils.nfskjnkkk
        val advertiseEntity = try {
            Gson().fromJson(json, AdvertiseEntity::class.java)
        } catch (e: Exception) {
            null
        }
        Log.e("TAG", "initializeAdConfig:advertiseEntity data :$advertiseEntity")
        startOpenBOIBOIUBU.initializeSource(advertiseEntity?.start)
        interHaHaHaOPNNOPIN.initializeSource(advertiseEntity?.inter)
        interHaHaHaOPNNOPIN2.initializeSource(advertiseEntity?.inter2)

        mainNativeHome.initializeSource(advertiseEntity?.nnnhh)
        mainNativeEnd.initializeSource(advertiseEntity?.nnnee)
        snlveijnaielv = advertiseEntity?.showMax ?: 0
        clickLimitBKUBOUIBI = advertiseEntity?.clickMax ?: 0
    }

    fun countAD(s: Boolean = false, c: Boolean = false) {
        val showaaaaa = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) + "show"
        val showcccccccc =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) + "click"
        val dayShow: Int = DataKeyUtils.getAdShowNumFun(showaaaaa)
        val dayClick: Int = DataKeyUtils.getAdClickNumFun(showcccccccc)
        if (s && dayShow < snlveijnaielv) {
            DataKeyUtils.setAdShowNumFun(showaaaaa, dayShow + 1)
        }
        if (c && dayClick < clickLimitBKUBOUIBI) {
            DataKeyUtils.setAdClickNumFun(showcccccccc, dayClick + 1)
        }
    }


    private fun show(): Boolean {
        val showaaaaa = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) + "show"
        return DataKeyUtils.getAdClickNumFun(showaaaaa) < snlveijnaielv
    }

    private fun click(): Boolean {
        val showcccccccc =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) + "click"
        return DataKeyUtils.getAdClickNumFun(showcccccccc) < clickLimitBKUBOUIBI
    }

    fun canShowAD(): Boolean {
        if (!show() || !click()) {
            Log.e("TAG", "canShowAD: AD LIMIT")
            val type =  if(!show()){"show"}else{"click"}
            CanDataUtils.postPointData(
                "antur16",
                "qu",
                type,
            )
        }
        return show() && click()
    }

    fun showOpenAdIfCan(activity: StartActivity, cancelFun: () -> Unit,jumpToNext: () -> Unit) {
        if (startOpenBOIBOIUBU.haveCache && activity.isActivityResumed()) {
            activity.job?.cancel()
            cancelFun()
            startOpenBOIBOIUBU.showFullScreenAdBIUYBUI(activity) {
                jumpToNext()
            }
        }
    }

    fun showConnectAdIfCan(activity: MainActivity, nextFun: () -> Unit) {
        if (interHaHaHaOPNNOPIN.haveCache && activity.isActivityResumed()) {
            activity.jobConnect?.cancel()
            activity.lifecycleScope.launch(Dispatchers.Main) {
                activity.showIntAd = true
                delay(1000)
                activity.showIntAd = false
                interHaHaHaOPNNOPIN.showFullScreenAdBIUYBUI(activity) {
                    nextFun()
                }
            }
        }
    }

    fun ComponentActivity.isActivityResumed(): Boolean {
        return Lifecycle.State.RESUMED == this.lifecycle.currentState
    }
}
