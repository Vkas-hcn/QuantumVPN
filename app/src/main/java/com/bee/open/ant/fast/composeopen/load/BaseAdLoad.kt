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
    val interHaHaHaOPNNOPINRE = NomadicLoad(ADType.INNNNNNNN_RE)
    val mainNativeHome = NomadicLoad(ADType.NNNAAAVVV_HHH)
    val mainNativeEnd = NomadicLoad(ADType.NNNAAAVVV_EEE)

    val startOpenBOIBOIUBUDis = NomadicLoad(ADType.FULL_One_Dis)
    val interHaHaHaOPNNOPIN2Dis = NomadicLoad(ADType.INNNNNNNN_2_Dis)
    val interHaHaHaOPNNOPINREDis = NomadicLoad(ADType.INNNNNNNN_RE_Dis)
    val mainNativeHomeDis = NomadicLoad(ADType.NNNAAAVVV_HHH_Dis)
    val mainNativeEndDis = NomadicLoad(ADType.NNNAAAVVV_EEE_Dis)
    fun initializeAdConfig(adConfigJson: String? = null) {
        var json = adConfigJson
        if (json.isNullOrBlank()) json = DataKeyUtils.nfskjnkkk
        val advertiseEntity = try {
            Gson().fromJson(json, AdvertiseEntity::class.java)
        } catch (e: Exception) {
            null
        }
        val advertiseEntityInFor = if (App.isVpnState == 2) {
            advertiseEntity?.server
        } else {
            advertiseEntity?.nonserver
        }
        //open
        startOpenBOIBOIUBU.initializeSource(advertiseEntityInFor?.start)
        //connect
        interHaHaHaOPNNOPIN.initializeSource(advertiseEntityInFor?.inter)
        //list back
        interHaHaHaOPNNOPIN2.initializeSource(advertiseEntityInFor?.inter2)
        //result back
        interHaHaHaOPNNOPINRE.initializeSource(advertiseEntityInFor?.interRe)
        // home native
        mainNativeHome.initializeSource(advertiseEntityInFor?.nnnhh)
        // result native
        mainNativeEnd.initializeSource(advertiseEntityInFor?.nnnee)

        // Dis open
        startOpenBOIBOIUBUDis.initializeSource(advertiseEntityInFor?.start)
        //Dis list back
        interHaHaHaOPNNOPIN2Dis.initializeSource(advertiseEntityInFor?.inter2)
        // Dis result back
        interHaHaHaOPNNOPINREDis.initializeSource(advertiseEntityInFor?.interRe)
        // Dis home native
        mainNativeHomeDis.initializeSource(advertiseEntityInFor?.nnnhh)
        // Dis result native
        mainNativeEndDis.initializeSource(advertiseEntityInFor?.nnnee)
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
            val type = if (!show()) {
                "show"
            } else {
                "click"
            }
            CanDataUtils.postPointData(
                "antur16",
                "qu",
                type,
            )
        }
        return show() && click()
    }

    fun showOpenAdIfCan(activity: StartActivity, cancelFun: () -> Unit, jumpToNext: () -> Unit) {
        val startOpen = getStartOpenAdData()
        if (startOpen.haveCache && activity.isActivityResumed()) {
            activity.job?.cancel()
            cancelFun()
            startOpen.showFullScreenAdBIUYBUI(activity) {
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

    fun showDialogAdIfCan(activity: ResultActivity, nextFun: () -> Unit) {
        if (interHaHaHaOPNNOPIN.haveCache && activity.isActivityResumed()) {
            activity.jobDialog?.cancel()
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

    fun getStartOpenAdData(): NomadicLoad {
        return if (App.isVpnState == 2) {
            startOpenBOIBOIUBU
        } else {
            startOpenBOIBOIUBUDis
        }
    }
    fun getInterListAdData(): NomadicLoad {
        return if (App.isVpnState == 2) {
            interHaHaHaOPNNOPIN2
        } else {
            interHaHaHaOPNNOPIN2Dis
        }
    }

    fun getInterResultAdData(): NomadicLoad {
        return if (App.isVpnState == 2) {
            interHaHaHaOPNNOPINRE
        } else {
            interHaHaHaOPNNOPINREDis
        }
    }
    fun getMainNativeAdData(): NomadicLoad {
        return if (App.isVpnState == 2) {
            mainNativeHome
        } else {
            mainNativeHomeDis
        }
    }

    fun getEndNativeAdData(): NomadicLoad {
        return if (App.isVpnState == 2) {
            mainNativeEnd
        } else {
            mainNativeEndDis
        }
    }

}
