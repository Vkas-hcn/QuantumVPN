package com.bee.open.ant.fast.composeopen.load

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.google.gson.Gson
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object FBAD {

    fun setADData() {
        if (!FBADUtils.isHaveHistoryData()) {
            BaseAdLoad.initializeAdConfig(DataKeyUtils.nfskjnkkk)
        }
    }

    fun checkADData(result: (Boolean) -> Unit = { true }) {
        if (FBADUtils.serverADData != null) {
            result.invoke(true)
        } else result.invoke(false)
    }

    fun checkADDataWithServer() {
        if (FBADUtils.serverADData == null) {
            setADData()
        }
    }


    fun getFirebaseStringADData() {
        if (DataKeyUtils.adOpenData.isNotEmpty() && DataKeyUtils.adOpenData.isNotBlank()) {
            try {
                val listAd = String(
                    android.util.Base64.decode(
                        DataKeyUtils.adOpenData,
                        android.util.Base64.DEFAULT
                    )
                )
                val gson = Gson()
                val resultBean: AdvertiseEntity? =
                    gson.fromJson(listAd, AdvertiseEntity::class.java)
                if (resultBean?.server != null &&resultBean.nonserver!=null) {
                    FBADUtils.serverADData = resultBean
                    BaseAdLoad.initializeAdConfig(listAd)
                    DataKeyUtils.adHistory = listAd
                }
                FBADUtils.isGetADData = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showVpnPermission(ac: ComponentActivity, checkVpnPermissionFun: () -> Unit) {
        var type = false
        DataKeyUtils.firstDialogState = DataKeyUtils.firstDialogState2
        if(DataKeyUtils.firstDialogState){return}
        ac.lifecycleScope.launch {
            delay(500)
            while (isActive) {
                if ((DishNomadicLoad.showAdBlacklist()) && DishNomadicLoad.getAutoConnectData() && !DataKeyUtils.firstDialogState && !type) {
                    Log.e("TAG", "showVpnPermission: ")
                    type = true
                    checkVpnPermissionFun()
                    cancel()
                }
                delay(200)
            }
        }
    }
}