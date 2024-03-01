package com.bee.open.ant.fast.composeopen.data

import com.bee.open.ant.fast.composeopen.app.App
import com.tencent.mmkv.MMKV

class DataKeyUtils {
    companion object{
        const val VPN_DATA_URL = "https://test.quantumsphereonline.com/WgNihdyrk/dJcpwZFkiK/fseqUqCrz/"
        const val proxyUrl = "https://baidu.com"
        const val blackUrl = "https://rubbish.quantumsphereonline.com/goal/fs"
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
    }

}