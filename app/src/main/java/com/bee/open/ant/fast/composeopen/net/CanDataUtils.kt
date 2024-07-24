package com.bee.open.ant.fast.composeopen.net

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.ReferrerDetails
import com.bee.open.ant.fast.composeopen.BuildConfig
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.EveryADBean
import com.facebook.appevents.AppEventsLogger
import com.facebook.appevents.internal.AppEventUtility.getAppVersion
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Currency
import java.util.Locale
import java.util.UUID

object CanDataUtils {

    private fun getTopLevelJsonData(
        haveAd: Boolean = false,
        adBean: EveryADBean? = null
    ): JSONObject {
        val jsonData = JSONObject()

        jsonData.apply {
            if (haveAd) {
                val loadCity = adBean?.qtv_load_city ?: "null"
                val showCity = adBean?.qtv_show_city ?: "null"
                put("tapioca", JSONObject().apply {
                    put("gal", loadCity)
                    put("dfr", showCity)
                })
            }
            put("unify", JSONObject().apply {
                //ip
                put("mien", DataKeyUtils.tba_ip_data)
                //os_country
                put("armchair", Locale.getDefault().country)
                //system_language
                put("velours", "${Locale.getDefault().language}_${Locale.getDefault().country}")
                //bundle_id
                put("pearce", App.getVpnInstance().packageName)
                //distinct_id
                put("woodhen", DataKeyUtils.tba_id_data)
            })
            put("floyd", JSONObject().apply {
                //log_id
                put("baritone", UUID.randomUUID().toString())
            })
            put("hand", JSONObject().apply {
                //android_id
                put("elegant", "1")
                //client_ts
                put("hemlock", System.currentTimeMillis())
                //os
                put("raunchy", "wold")
                //operator
                put("bookmark", "111")
                //device_model
                put("shuddery", "1")
            })

            put("thud", JSONObject().apply {
                //os_version
                put("million", "1")
                //manufacturer
                put("annale", "hw")
                //app_version
                put("camp", getAppVersion())
            })
        }
        return jsonData
    }

    fun getSessionJson(): String {
        return getTopLevelJsonData().apply {
            put("dade", "mast")
        }.toString()
    }

    fun getInstallJson(context: Context, referrerDetails: ReferrerDetails): String {
        return getTopLevelJsonData().apply {
            put("celebes", JSONObject().apply {
                //build
                put("negligee", "build/${Build.ID}")

                //referrer_url
                put("settle", referrerDetails.installReferrer)

                //install_version
                put("uncouth", referrerDetails.installVersion)

                //user_agent
                put("impart", getWebDefaultUserAgent(context))

                //lat
                put("concoct", getLimitTracking(context))

                //referrer_click_timestamp_seconds
                put("soapsud", referrerDetails.referrerClickTimestampSeconds)

                //install_begin_timestamp_seconds
                put("failure", referrerDetails.installBeginTimestampSeconds)

                //referrer_click_timestamp_server_seconds
                put("perth", referrerDetails.referrerClickTimestampServerSeconds)

                //install_begin_timestamp_server_seconds
                put("sugar", referrerDetails.installBeginTimestampServerSeconds)

                //install_first_seconds
                put("comb", getFirstInstallTime(context))

                //last_update_seconds
                put("gardner", getLastUpdateTime(context))
            })

        }.toString()
    }

    private fun getAdAllJson(
        adValue: AdValue,
        responseInfo: ResponseInfo?,
        adBean: EveryADBean?,
    ): String {

        return getTopLevelJsonData(true, adBean).apply {
            put("tungsten", JSONObject().apply {
                //ad_pre_ecpm
                put("mezzo", adValue.valueMicros)
                //currency
                put("fraught", adValue.currencyCode)
                //ad_network
                put(
                    "schooner",
                    responseInfo?.mediationAdapterClassName
                )
                //ad_source
                put("pierre", "admob")
                //ad_code_id
                put("sciatica", adBean?.adIdKKKK)
                //ad_pos_id
                put("knobby", adBean?.where)
                //ad_rit_id
                put("shoemake", null)
                //ad_sense
                put("sou", null)
                //ad_format
                put("assault", adBean?.adYype)
                //precision_type
                put("fickle", getPrecisionType(adValue.precisionType))
                //ad_load_ip
                put("holmdel", adBean?.qtv_load_ip ?: "")
                //ad_impression_ip
                put("biennial", adBean?.qtv_show_ip ?: "")
                //ad_sdk_ver
                put("hewett", responseInfo?.responseId)
            })

        }.toString()
    }

    fun getTbaDataJson(name: String): String {
        return getTopLevelJsonData().apply {
            put("dade", name)
        }.toString()
    }

    fun getTbaTimeDataJson(
        name: String,
        parameterName1: String,
        parameterValue1: Any,
        parameterName2: String?,
        parameterValue2: Any?,
    ): String {
        return getTopLevelJsonData().apply {
            put("dade", name)
            put(name, JSONObject().apply {
                put(parameterName1, parameterValue1)
                if (parameterName2 != null) {
                    put(parameterName2, parameterValue2)
                }
            })
        }.toString()
    }


    private fun getWebDefaultUserAgent(context: Context): String {
        return try {
            WebSettings.getDefaultUserAgent(context)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getFirstInstallTime(context: Context): Long {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.firstInstallTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getLastUpdateTime(context: Context): Long {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.lastUpdateTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getPrecisionType(precisionType: Int): String {
        return when (precisionType) {
            0 -> {
                "UNKNOWN"
            }

            1 -> {
                "ESTIMATED"
            }

            2 -> {
                "PUBLISHER_PROVIDED"
            }

            3 -> {
                "PRECISE"
            }

            else -> {
                "UNKNOWN"
            }
        }
    }

    private fun getLimitTracking(context: Context): String {
        return try {
            if (AdvertisingIdClient.getAdvertisingIdInfo(context).isLimitAdTrackingEnabled) {
                "mitosis"
            } else {
                "cambric"
            }
        } catch (e: Exception) {
            "cambric"
        }
    }

    fun postSessionData() {
        val json = getSessionJson()
        Log.e("TBA", "json-getSessionJson--->${json}")
        try {
            GetNetDataUtils.postTbaData(
                DataKeyUtils.tbaUrl,
                json,
                {
                    Log.e("TAG", "Session事件上报-成功->${it}")
                }, {
                    Log.e("TAG", "Session事件上报-失败=$it")
                })
        } catch (e: Exception) {
            Log.e("TAG", "Session事件上报-失败=$e")
        }
    }

    fun postInstallData(context: Context, referrerDetails: ReferrerDetails) {
        if (DataKeyUtils.tba_install) {
            return
        }
        val json = getInstallJson(context, referrerDetails)
        Log.e("TBA", "json-getInstallJson--->${json}")
        try {
            GetNetDataUtils.postTbaData(
                DataKeyUtils.tbaUrl,
                json,
                {
                    Log.e("TAG", "Install事件上报-成功->${it}")
                    DataKeyUtils.tba_install = true
                }, {
                    Log.e("TAG", "Install事件上报-失败=$it")
                })
        } catch (e: Exception) {
            Log.e("TAG", "Install事件上报-失败=$e")
        }
    }

    fun postAdAllData(
        adValue: AdValue,
        responseInfo: ResponseInfo?,
        adBean: EveryADBean,
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val json = getAdAllJson(adValue, responseInfo, adBean)
            Log.e("TBA", "${adBean.where}-Ad-Json--->${json}")
            try {
                GetNetDataUtils.postTbaData(
                    DataKeyUtils.tbaUrl,
                    json,
                    {
                        Log.e("TAG", "${adBean.where}-广告事件上报-成功->${it}")
                    }, {
                        Log.e("TAG", "${adBean.where}-广告事件上报-失败=$it")
                    })
            } catch (e: Exception) {
                Log.e("TAG", "${adBean.where}-广告事件上报-失败=$e")
            }
        }
    }

    fun postPointData(
        name: String,
        key: String? = null,
        keyValue: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null
    ) {
        val pointJson = if (key != null && keyValue != null) {
            getTbaTimeDataJson(name, key, keyValue, key2, keyValue2)
        } else {
            getTbaDataJson(name)
        }
        Log.e("TBA", "${name}-打点--Json--->${pointJson}")
        try {
            GetNetDataUtils.postTbaData(
                DataKeyUtils.tbaUrl,
                pointJson,
                {
                    Log.e("TAG", "${name}-打点事件上报-成功->${it}")
                }, {
                    Log.e("TAG", "${name}-打点事件上报-失败=$it")
                })
        } catch (e: Exception) {
            Log.e("TAG", "${name}-打点事件上报-失败=$e")
        }
    }

    fun beforeLoadQTV(ufDetailBean: EveryADBean): EveryADBean {
        var data = false
        if (App.isVpnState == 2 && !DataKeyUtils.spoiler_data) {
            ufDetailBean.qtv_load_ip = DataKeyUtils.tba_vpn_ip
            ufDetailBean.qtv_load_city = DataKeyUtils.tba_vpn_city
        } else {
            data = true
        }
        if (data) {
            ufDetailBean.qtv_load_ip = DataKeyUtils.tba_ip_data
            ufDetailBean.qtv_load_city = "null"
        }
        return ufDetailBean
    }


    fun afterLoadQTV(ufDetailBean: EveryADBean): EveryADBean {
        var data = false
        if (App.isVpnState == 2 && !DataKeyUtils.spoiler_data) {
            ufDetailBean.qtv_show_ip = DataKeyUtils.tba_vpn_ip
            ufDetailBean.qtv_show_city = DataKeyUtils.tba_vpn_city
        } else {
            data = true
        }
        if (data) {
            ufDetailBean.qtv_show_ip = DataKeyUtils.tba_ip_data
            ufDetailBean.qtv_show_city = "null"
        }
        return ufDetailBean
    }

    fun toPointAdQTV(
        adValue: AdValue,
        responseInfo: ResponseInfo?,
    ) {
        val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
        adRevenue.setRevenue(
            adValue.valueMicros / 1000000.0,
            adValue.currencyCode
        )
        adRevenue.setAdRevenueNetwork(responseInfo?.mediationAdapterClassName)
        Adjust.trackAdRevenue(adRevenue)
        if (!BuildConfig.DEBUG) {
            AppEventsLogger.newLogger(App.getVpnInstance()).logPurchase(
                (adValue.valueMicros / 1000000.0).toBigDecimal(), Currency.getInstance("USD")
            )
        } else {
            Log.d("TBA", "purchase打点--value=${adValue.valueMicros}")
        }
    }
    private fun isNetworkReachable(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            process.waitFor()
            process.destroy()

            val result = output.toString()
            val state = result.contains("1 packets transmitted, 1 received")
            Log.e("TAG", "isNetworkReachable: ${state}")
            return state
        } catch (e: Exception) {
            Log.e("TAG", "isNetworkReachable: ----fasle")

            false
        }
    }
    fun antur10() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("TAG", "antur10: 开始检测")
            val netState = isNetworkReachable()
            val isHaveData = if (netState) {
                Log.e("TAG", "antur10: 开始检测-1")
                "1"
            } else {
                Log.e("TAG", "antur10: 开始检测-2")
                "2"
            }
            postPointData(
                "antur10",
                "qu",
                BaseAdLoad.interHaHaHaOPNNOPIN.haveCache.toString(),
                "hh",
                isHaveData
            )
        }
    }

    fun antur12() {
        val text = if (App.isVpnState ==2) {
            "cont"
        } else {
            "dis"
        }
        postPointData(
            "antur12", "qu",
            BaseAdLoad.interHaHaHaOPNNOPIN.haveCache.toString(),
            "op",
            text
        )
    }

    fun antur14(adBean: EveryADBean) {
        postPointData(
            "antur14",
            "qu",
            "${adBean.adIdKKKK}+${App.top_activity_Quan?.javaClass?.simpleName}",
            "op",
            (App.isVpnState==2).toString()
        )
        if ((App.isVpnState==2) && !DataKeyUtils.spoiler_data) {
            postPointData(
                "antur22",
                "qu",
                adBean.where,
            )
        }
        if (App.isVpnState==2) {
            postPointData(
                "antur23",
                "qu",
                adBean.where,
            )
        }

    }

    fun antur15(adBean: EveryADBean) {
        postPointData(
            "antur15",
            "qu",
            "${adBean.adIdKKKK}+${App.top_activity_Quan?.javaClass?.simpleName}",
        )
    }

    fun antur17(adBean: EveryADBean, errorString: String) {
        postPointData(
            "antur17",
            "qu",
            "${adBean.adIdKKKK}+${errorString}",
        )
    }
}