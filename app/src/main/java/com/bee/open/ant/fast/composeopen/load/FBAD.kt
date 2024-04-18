package com.bee.open.ant.fast.composeopen.load

import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.google.gson.Gson

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
                if (resultBean != null) {
                    FBADUtils.isGetADData = true
                    FBADUtils.serverADData = resultBean
                    BaseAdLoad.initializeAdConfig(listAd)
                    DataKeyUtils.adHistory = listAd
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}