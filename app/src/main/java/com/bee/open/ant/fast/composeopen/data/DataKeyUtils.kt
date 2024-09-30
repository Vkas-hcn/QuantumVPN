package com.bee.open.ant.fast.composeopen.data

import com.bee.open.ant.fast.composeopen.BuildConfig
import com.bee.open.ant.fast.composeopen.app.App
import com.tencent.mmkv.MMKV

class DataKeyUtils {
    companion object {
        const val VPN_DATA_URL =
            "https://test.quantumsphereonline.com/WgNihdyrk/dJcpwZFkiK/fseqUqCrz/"
        const val proxyUrl = "https://baidu.com"
        const val blackUrl = "https://rubbish.quantumsphereonline.com/goal/fs"
        val tbaUrl = if (BuildConfig.DEBUG) {
            "https://test-backward.quantumsphereonline.com/jubilate/stun/mulct"
        } else {
            "https://backward.quantumsphereonline.com/czarina/ding"
        }

        var adOpenKey = "vointt"
        var configOpenKey = "sad"

        val save = App.saveUtils
        var firstDialogState = false
            set(value) {
                save.encode("firstDialogState", value)
                field = value
            }
            get() = save.decodeBool("firstDialogState", false)
        var firstDialogState2 = false
            set(value) {
                save.encode("firstDialogState2", value)
                field = value
            }
            get() = save.decodeBool("firstDialogState2", false)

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

        var tba_id_data = ""
            set(value) {
                save.encode("tba_id_data", value)
                field = value
            }
            get() = save.decodeString("tba_id_data", "") ?: ""

        var tba_ip_data = ""
            set(value) {
                save.encode("tba_ip_data", value)
                field = value
            }
            get() = save.decodeString("tba_ip_data", "") ?: ""


        var tba_vpn_ip = ""
            set(value) {
                save.encode("tba_vpn_ip", value)
                field = value
            }
            get() = save.decodeString("tba_vpn_ip", "") ?: ""

        var tba_vpn_city = ""
            set(value) {
                save.encode("tba_vpn_city", value)
                field = value
            }
            get() = save.decodeString("tba_vpn_city", "") ?: ""

        var tba_install = false
            set(value) {
                save.encode("tba_install", value)
                field = value
            }
            get() = save.decodeBool("tba_install", false)

        var userAdType = false
            set(value) {
                save.encode("userAdType", value)
                field = value
            }
            get() = save.decodeBool("userAdType", false)


        var ad_j_v = false
            set(value) {
                save.encode("ad_j_v", value)
                field = value
            }
            get() = save.decodeBool("ad_j_v", false)

        var buying_volume = false
            set(value) {
                save.encode("buying_volume", value)
                field = value
            }
            get() = save.decodeBool("buying_volume", false)

        var black_data_up_type = false
            set(value) {
                save.encode("black_data_up_type", value)
                field = value
            }
            get() = save.decodeBool("black_data_up_type", false)

        var service_q_x_type = false
            set(value) {
                save.encode("service_q_x_type", value)
                field = value
            }
            get() = save.decodeBool("service_q_x_type", false)

        var service_q_x_type2 = false
            set(value) {
                save.encode("service_q_x_type2", value)
                field = value
            }
            get() = save.decodeBool("service_q_x_type2", false)

        var autoConnect = false
            set(value) {
                save.encode("autoConnect", value)
                field = value
            }
            get() = save.decodeBool("autoConnect", false)

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
    "nunn": 200,
    "comping": 5,
    "server": {
        "tuop": [
            {
                "tom": "ca-app-pub-3940256099942544/9257395921",
                "book": "admob",
                "keep": "plai",
                "fa_c_oute": 2,
                "where": "tuop"
            }
        ],
        "intu": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 2,
                "where": "intu"
            }
        ],
        "tintuba": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 2,
                "where": "tintuba"
            }
        ],
        "basex": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 2,
                "where": "basex"
            }
        ],
        "saxc": [
            {
                "tom": "ca-app-pub-3940256099942544/2247696110",
                "book": "admob",
                "keep": "nnnee",
                "fa_c_oute": 2,
                "where": "saxc"
            }
        ],
        "mstan": [
            {
                "tom": "ca-app-pub-3940256099942544/2247696110",
                "book": "admob",
                "keep": "nnnee",
                "fa_c_oute": 2,
                "where": "mstan"
            }
        ]
    },
    "nonserver": {
        "tuop": [
            {
                "tom": "ca-app-pub-3940256099942544/9257395921",
                "book": "admob",
                "keep": "plai",
                "fa_c_oute": 1,
                "where": "tuop"
            }
        ],
        "intu": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 1,
                "where": "intu"
            }
        ],
        "tintuba": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 1,
                "where": "tintuba"
            }
        ],
        "basex": [
            {
                "tom": "ca-app-pub-3940256099942544/1033173712",
                "book": "admob",
                "keep": "ity",
                "fa_c_oute": 1,
                "where": "basex"
            }
        ],
        "saxc": [
            {
                "tom": "ca-app-pub-3940256099942544/2247696110",
                "book": "admob",
                "keep": "nnnee",
                "fa_c_oute": 1,
                "where": "saxc"
            }
        ],
        "mstan": [
            {
                "tom": "ca-app-pub-3940256099942544/2247696110",
                "book": "admob",
                "keep": "nnnee",
                "fa_c_oute": 1,
                "where": "mstan"
            }
        ]
    }
}
        """

        const val configOpenLocal = """
  {
    "brisk":"3",
    "fooey":"20&20",
    "kate":"0",
    "guide":"2",
    "qua8":"1",
    "hang": "2",
    "miak": "1",
    "ssfd":""
  }
        """
    }

}