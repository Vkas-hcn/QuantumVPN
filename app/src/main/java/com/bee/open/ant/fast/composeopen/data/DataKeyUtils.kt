package com.bee.open.ant.fast.composeopen.data

import com.bee.open.ant.fast.composeopen.app.App
import com.tencent.mmkv.MMKV

class DataKeyUtils {
    companion object {
        const val VPN_DATA_URL =
            "https://test.quantumsphereonline.com/WgNihdyrk/dJcpwZFkiK/fseqUqCrz/"
        const val proxyUrl = "https://baidu.com"
        const val blackUrl = "https://rubbish.quantumsphereonline.com/goal/fs"
        var adOpenKey = "vointt"
        var configOpenKey = "sad"

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
    "nunn": 20,
    "comping": 5,
    "tuop": [
        {
            "tom": "ca-app-pub-2001364143953351/3192287239",
            "book": "admob",
            "keep": "plai",
            "fa_c_oute": 2,
            "where": "tuop"
        }
    ],
    "intu": [
        {
            "tom": "ca-app-pub-2001364143953351/3052686431",
            "book": "admob",
            "keep": "ity",
            "fa_c_oute": 2,
            "where": "intu"
        }
    ],
    "tintuba": [
        {
            "tom": "ca-app-pub-2001364143953351/1810072742",
            "book": "admob",
            "keep": "ity",
            "fa_c_oute": 2,
            "where": "tintuba"
        }
    ]
}
        """

        const val configOpenLocal = """
  {
    "hang": "1",
    "miak": "1"
  }
        """
    }

}