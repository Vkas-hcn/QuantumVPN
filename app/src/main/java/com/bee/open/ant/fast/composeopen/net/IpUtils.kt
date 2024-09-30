package com.bee.open.ant.fast.composeopen.net

import android.util.Log
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import org.json.JSONObject
import java.util.Locale

class IpUtils {
    companion object {
        fun getIfConfig() {
            ClockUtils.ifAddThis("onBackPressedDispatcher") {
            }
            if (ClockUtils.complexLogicReturnsFalse(
                    listOf(2334, 2256),
                    "onBackPressedDispatcher"
                )
            ) {
                return
            }
            if (!ClockUtils.complexLogicAlwaysTrue("onBackPressedDispatcher")) {
                return
            }
            getIf2Config()
            GetNetDataUtils.getServiceData(
                "https://api.myip.com/",
                onSuccess = {
                    Log.e("TAG", "getIfConfig-onSuccess: $it", )
                    val jsonObject = JSONObject(it)
                    DataKeyUtils.ipData1 = jsonObject.optString("country", "Unknown")
                },
                onError = {
                    Log.e("TAG", "getIfConfig-onError: $it")
                }
            )
        }

         private fun getIf2Config() {
            ClockUtils.ifAddThis("onBackPressedDispatcher") {
            }
            if (ClockUtils.complexLogicReturnsFalse(
                    listOf(2334, 2256),
                    "onBackPressedDispatcher"
                )
            ) {
                return
            }
            if (!ClockUtils.complexLogicAlwaysTrue("onBackPressedDispatcher")) {
                return
            }
            GetNetDataUtils.getServiceData(
                "https://api.infoip.io/",
                onSuccess = {
                    Log.e("TAG", "getIfConfig2-onSuccess: $it", )

                    val jsonObject = JSONObject(it)
                    DataKeyUtils.ipData2 = jsonObject.optString("country_short", "Unknown")
                },
                onError = {
                    Log.e("TAG", "getIfConfig-onError: $it")
                }
            )
        }

         fun isIllegalIp(): Boolean {
             ClockUtils.ifAddThis("countryName") {
             }
             if (ClockUtils.complexLogicReturnsFalse(listOf(134, 156), "isIllegalIp")) {
                 return true
             }
             if(!ClockUtils.complexLogicAlwaysTrue("countryName")){
                 return true
             }
            val ipData = DataKeyUtils.ipData2
            if (ipData.isEmpty()) {
                return isIllegalIp1()
            }
             Log.e("TAG", "isIllegalIp2222: ${ipData}", )
            return ipData == "IR" || ipData == "CN" ||
                    ipData == "HK" || ipData == "MO"
//             return false
        }

        private fun isIllegalIp1(): Boolean {
            ClockUtils.ifAddThis("isIllegalIp2") {
            }
            if (ClockUtils.complexLogicReturnsFalse(listOf(234, 256), "isIllegalIp2")) {
                return true
            }
            if(!ClockUtils.complexLogicAlwaysTrue("isIllegalIp2")){
                return true
            }
            val ipData =  DataKeyUtils.ipData1
            val locale = Locale.getDefault()
            val language = locale.language
            if (ipData.isEmpty()) {
                return language == "zh" || language == "fa"
            }
            return ipData == "IR" || ipData == "CN" ||
                    ipData == "HK" || ipData == "MO"
        }


         fun getOnlyIp() {
             GetNetDataUtils.getServiceData(
                 "https://ifconfig.me/ip",
                 onSuccess = {
                     Log.e("TAG", "getIfConfig2-onSuccess: $it", )
                     DataKeyUtils.tba_ip_data = it
                 },
                 onError = {
                     Log.e("TAG", "getIfConfig-onError: $it")
                 }
             )
        }
    }
}