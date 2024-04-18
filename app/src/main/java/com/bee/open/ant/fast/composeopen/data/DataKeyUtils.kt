package com.bee.open.ant.fast.composeopen.data

import com.bee.open.ant.fast.composeopen.app.App
import com.tencent.mmkv.MMKV

class DataKeyUtils {
    companion object {
        const val VPN_DATA_URL =
            "https://test.quantumsphereonline.com/WgNihdyrk/dJcpwZFkiK/fseqUqCrz/"
        const val proxyUrl = "https://baidu.com"
        const val blackUrl = "https://rubbish.quantumsphereonline.com/goal/fs"
        var adOpenKey = "adOpenKey"
        var configOpenKey = "configOpenKey"

        val save = App.saveUtils

        var uuid_open = ""
            set(value) {
                save.encode("uuid_open", value)
                field = value
            }
            get() = save.decodeString("uuid_open", "") ?: ""
        var black_data = ""
            set(value) {
                save.encode("black_data", value)
                field = value
            }
            get() = save.decodeString("black_data", "") ?: ""
        var ipData1 = ""
            set(value) {
                save.encode("ipData1", value)
                field = value
            }
            get() = save.decodeString("ipData1", "") ?: ""
        var ipData2 = ""
            set(value) {
                save.encode("ipData2", value)
                field = value
            }
            get() = save.decodeString("ipData2", "") ?: ""

        var vpnDataList = ""
            set(value) {
                save.encode("vpnData", value)
                field = value
            }
            get() = save.decodeString("vpnData", "") ?: ""

        var nowVpnServiceData = ""
            set(value) {
                save.encode("nowVpnServiceData", value)
                field = value
            }
            get() = save.decodeString("nowVpnServiceData", "") ?: ""

        //选中vpn服务器
        var selectVpnServiceData = ""
            set(value) {
                save.encode("selectVpnServiceData", value)
                field = value
            }
            get() = save.decodeString("selectVpnServiceData", "") ?: ""


        var adHistory = ""
            set(value) {
                save.encode("adHistory", value)
                field = value
            }
            get() = save.decodeString("adHistory", "") ?: ""

        var adOpenData = ""
            set(value) {
                save.encode("adOpenData", value)
                field = value
            }
            get() = save.decodeString("adOpenData", "") ?: ""
        var configOpenData = ""
            set(value) {
                save.encode("configOpenData", value)
                field = value
            }
            get() = save.decodeString("configOpenData", "") ?: ""

        var spoiler_data = false
            set(value) {
                save.encode("spoiler_data", value)
                field = value
            }
            get() = save.decodeBool("spoiler_data", false)

        fun setAdShowNumFun(key: String, data: Int) {
            save.encode(key, data)
        }

        fun getAdShowNumFun(key: String): Int {
            return save.decodeInt(key, 0)
        }

        fun setAdClickNumFun(key: String, data: Int) {
            save.encode(key, data)
        }

        fun getAdClickNumFun(key: String): Int {
            return save.decodeInt(key, 0)
        }

        const val nfskjnkkk = """
            {
    "showNum": 50,
    "clickNum": 1,
    "open_start": [
        {
            "ad_id": "ca-app-pub-3940256099942544/9257395921",
            "ad_from": "admob",
            "ad_type": "plai",
            "cache_time": 50,
            "fa_c_oute": 2,
            "where": "open_start"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/9257395921xx",
            "ad_from": "admob",
            "ad_type": "plai",
            "cache_time": 50,
            "fa_c_oute": 3,
            "where": "open_start"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/8691691433",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 1,
            "where": "open_start"
        }
    ],
    "open_connect": [
        {
            "ad_id": "ca-app-pub-3940256099942544/8691691433xx",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 3,
            "where": "open_connect"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/1033173712",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 1,
            "where": "open_connect"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/8691691433",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 2,
            "where": "open_connect"
        }
    ],
    "open_back": [
        {
            "ad_id": "ca-app-pub-3940256099942544/8691691433",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 1,
            "where": "open_back"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/1033173712",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 2,
            "where": "open_back"
        },
        {
            "ad_id": "ca-app-pub-3940256099942544/8691691433xx",
            "ad_from": "admob",
            "ad_type": "ity",
            "cache_time": 50,
            "fa_c_oute": 3,
            "where": "open_back"
        }
    ]
}
        """

        const val configOpenLocal = """
  {
    "clockType": "1",
    "raoliu": "1"
  }
        """
    }

}