package com.bee.open.ant.fast.composeopen.load
import android.annotation.SuppressLint
import android.util.Log
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object FBADUtils {

    @SuppressLint("StaticFieldLeak")
    var remoteConfig: FirebaseRemoteConfig? = null
    var serverADData: AdvertiseEntity? = null
    var isGetADData: Boolean = false

    var isAppInit = true

    fun getFirebaseRemoteConfigData() {
        remoteConfig = Firebase.remoteConfig
        remoteConfig!!.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                DataKeyUtils.adOpenData =  remoteConfig?.getString(DataKeyUtils.adOpenKey)?:""
                DataKeyUtils.configOpenData =  remoteConfig?.getString(DataKeyUtils.configOpenKey)?:""
                Log.e("TAG", "getFirebaseRemoteConfigData: =${ DataKeyUtils.configOpenData}")
                initFacebookData()
                FBAD.getFirebaseStringADData()
            }
        }
    }

    private fun initFacebookData(){
        val fbId = GetServiceData.getLocalOpenData().ssfd
        if(!fbId.isNullOrBlank()){
            Log.e("TAG", "fbId=====: $fbId")
            FacebookSdk.setApplicationId(fbId)
            FacebookSdk.sdkInitialize(App.appContext)
            AppEventsLogger.activateApp(App.getVpnInstance())
        }
    }
    fun fourAppWait4SecondsToGetData() {
        MainScope().launch {
            var i = 1
            while (i < 8) {
                if (!isGetADData) FBAD.getFirebaseStringADData()
                delay(500)
                if (i == 7) {
                    FBAD.setADData()
                }
                i++
            }
        }
    }

    fun appCircleToRequestFireData() {
        MainScope().launch {
            while (true) {
                if (!isGetADData) {
                    FBAD.getFirebaseStringADData()
                }
                if (isGetADData) {
                    break
                }
                delay(1000)
            }
        }
    }

    fun isHaveHistoryData(): Boolean {
        val gson = Gson()
        val resultBean: AdvertiseEntity? = gson.fromJson(DataKeyUtils.adHistory, AdvertiseEntity::class.java)
        return if (DataKeyUtils.adHistory != "" && resultBean != null) {
            BaseAdLoad.initializeAdConfig(DataKeyUtils.adHistory)
            true
        } else false

    }
}