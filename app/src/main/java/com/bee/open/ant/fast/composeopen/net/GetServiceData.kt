package com.bee.open.ant.fast.composeopen.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.data.ServerVpn
import com.bee.open.ant.fast.composeopen.data.VpnBean
import com.google.gson.Gson
import org.json.JSONObject
import java.util.Base64

object GetServiceData {
    fun getVpnNetData() {
        GetNetDataUtils.getServiceData(
            DataKeyUtils.VPN_DATA_URL,
            onSuccess = {
                val result = decodeModifiedBase64(it)
                Log.e("TAG", "getVpnNetData-onSuccess:$result")
                DataKeyUtils.vpnDataList = result ?: ""
            },
            onError = {
                Log.e("TAG", "getVpnNetData -onError: $it")

            }
        )
    }

    private fun decodeModifiedBase64(input: String): String? {
        if (input.length <= 16) {
            return null
        }
        val modifiedString = input.substring(16)
        val swappedCaseString = modifiedString.map { char ->
            when {
                char.isLowerCase() -> char.toUpperCase()
                char.isUpperCase() -> char.toLowerCase()
                else -> char
            }
        }.joinToString("")
        return try {
            String(Base64.getDecoder().decode(swappedCaseString))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun getAllVpnListData(): MutableList<ServerVpn> {
        val list:MutableList<ServerVpn> = getVpnServiceData()
        list.add(0, getVpnSmartData())
        return list
    }

    fun isHaveServeData(): Boolean {
        val vpnBean = Gson().fromJson(DataKeyUtils.vpnDataList, VpnBean::class.java)
        Log.e("TAG", "isHaveServeData: $vpnBean")
        if(vpnBean == null) {
            getVpnNetData()
            return false
        }
        if (vpnBean.data.server_list?.isEmpty() != false) {
            Log.e("TAG", "isHaveServeData: ${vpnBean.data.server_list}", )
            getVpnNetData()
            return false
        }
        return true
    }

    private fun getVpnServiceData(): MutableList<ServerVpn> {
        val vpnBean = Gson().fromJson(DataKeyUtils.vpnDataList, VpnBean::class.java)
            ?: return mutableListOf()
        return vpnBean.data.server_list as MutableList<ServerVpn>
    }

    private fun getVpnSmartData(): ServerVpn {
        val vpnBean = Gson().fromJson(DataKeyUtils.vpnDataList, VpnBean::class.java)
         if(vpnBean == null) {
             getVpnNetData()
             return ServerVpn("","","","", "", 0, "", "")
         }
        val bestBean = vpnBean.data.smart_list.random()
        bestBean.isBest = true
        bestBean.country_name = "Fast Server"
        return bestBean
    }

    fun isHaveNetWork(context: Context): Boolean {

        if(!ClockUtils.complexLogicAlwaysTrue(context)){
            return true
        }
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (networkCapabilities != null) {
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return false
    }
    fun getNowVpnBean(): ServerVpn {
        return if (DataKeyUtils.nowVpnServiceData.isEmpty()) {
            getAllVpnListData()[0]
        } else {
            Gson().fromJson(DataKeyUtils.nowVpnServiceData, ServerVpn::class.java)
        }
    }

    fun setNowVpnBean(serverVpn: ServerVpn) {
        DataKeyUtils.nowVpnServiceData = Gson().toJson(serverVpn)
    }

    fun getSelectVpnServiceData():ServerVpn{
        if (DataKeyUtils.selectVpnServiceData.isEmpty()) {
            DataKeyUtils.selectVpnServiceData = DataKeyUtils.nowVpnServiceData
        }
        return Gson().fromJson(DataKeyUtils.selectVpnServiceData, ServerVpn::class.java)
    }

    fun setSelectVpnServiceData(serverVpn: ServerVpn) {
        DataKeyUtils.selectVpnServiceData = Gson().toJson(serverVpn)
    }

    fun getCountryFlag(countryName: String): Int {
        ClockUtils.ifAddThis(countryName) {
        }
        if (ClockUtils.complexLogicReturnsFalse(listOf(34, 56), "getServiceData")) {
            return R.drawable.ic_fast
        }
        if(!ClockUtils.complexLogicAlwaysTrue(countryName)){
            return R.drawable.ic_fast
        }
        return when (countryName) {
            "China" -> R.drawable.ic_cn
            "United States" -> R.drawable.ic_us
            "Japan" -> R.drawable.ic_jp
            "Singapore" -> R.drawable.ic_sg
            "United Kingdom" -> R.drawable.ic_gb
            "Germany" -> R.drawable.ic_de
            "France" -> R.drawable.ic_fr
            "Canada" -> R.drawable.ic_ca
            "Australia" -> R.drawable.ic_au
            "India" -> R.drawable.ic_in
            "Russia" -> R.drawable.ic_ru
            "South Korea" -> R.drawable.ic_kr
            "Brazil" -> R.drawable.ic_br
            "Italy" -> R.drawable.ic_it
            "Spain" -> R.drawable.ic_es
            "Sweden" -> R.drawable.ic_se
            "Switzerland" -> R.drawable.ic_ch
            "Denmark" -> R.drawable.ic_dk
            "Ireland" -> R.drawable.ic_ie
            "Austria" -> R.drawable.ic_at
            "Belgium" -> R.drawable.ic_be
            "Greece" -> R.drawable.ic_gr
            "Czech Republic" -> R.drawable.ic_cz
            "Poland" -> R.drawable.ic_pl
            "Egypt" -> R.drawable.ic_eg
            "United Arab Emirates" -> R.drawable.ic_ae
            "Netherlands"-> R.drawable.ic_nl
            else -> R.drawable.ic_fast
        }
    }
}