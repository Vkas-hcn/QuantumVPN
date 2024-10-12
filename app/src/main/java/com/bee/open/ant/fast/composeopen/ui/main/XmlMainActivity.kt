package com.bee.open.ant.fast.composeopen.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.databinding.ActivityMainBinding
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.load.FBAD
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.net.GetServiceData.startRotate
import com.bee.open.ant.fast.composeopen.net.GetServiceData.stopRotate
import com.bee.open.ant.fast.composeopen.net.IpUtils
import com.bee.open.ant.fast.composeopen.ui.end.XmlResultActivity
import com.bee.open.ant.fast.composeopen.ui.service.ServiceListActivity
import com.bee.open.ant.fast.composeopen.ui.web.WebActivity
import de.blinkt.openvpn.api.ExternalOpenVPNService
import de.blinkt.openvpn.api.IOpenVPNAPIService
import de.blinkt.openvpn.api.IOpenVPNStatusCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Button
import androidx.core.view.isVisible
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad.isActivityResumed
import com.bee.open.ant.fast.composeopen.net.MyService


class XmlMainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var mService: IOpenVPNAPIService? = null

    //    val connection = ShadowsocksConnection(true)
    lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>
    var vpnState by mutableStateOf(-1)  // 0 未连接 1 连接中 2 已连接 -1 未知
    var beforeVpnState by mutableStateOf(-2)
    var showIpDialog by mutableStateOf(false)
    var jobConnect: Job? = null
    var adJobDialogMain: Job? = null
    var mModelInt = 1
    private val timerViewModel: TimerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        clickFUn()
        getHomeNativeAd()
    }

    private fun initData() {
//        connection.connect(this, this)
//        DataStore.publicStore.registerChangeListener(this)
        binding.vpnGuide = App.isShow
        binding.vpnModel = App.vpnModel
        intIP()
        setVpnInfor()
        CanDataUtils.postPointData("antur2")
        try {
            this.bindService(
                Intent(this, ExternalOpenVPNService::class.java), mConnection, BIND_AUTO_CREATE
            )
        } catch (e: Exception) {
            Log.e("TAG", "main---- bindService: $e")
        }
        FBAD.showVpnPermission(this) {
            DataKeyUtils.firstDialogState = true
            clickVpn()
            CanDataUtils.postPointData("antur30")
        }
        DataKeyUtils.firstDialogState2 = true
        DishNomadicLoad.getSpoilerData()
        requestPermissionForResultVPN =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                requestPermissionForResult(it)
            }
        lifecycleScope.launch {
            timerViewModel.timerText.collect { time ->
                binding.tvTime.text = time
            }
        }
        // 启动服务
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        if (DishNomadicLoad.getBuyingShieldData()) {
            Log.e("TAG", "买量屏蔽广告show")
            binding.adLayout.isVisible = false
        }
    }


    private fun setVpnInfor() {
        binding.tvServiceName.text =
            "${GetServiceData.getNowVpnBean().country_name}, ${GetServiceData.getNowVpnBean().city}"
        binding.tvCName.text = GetServiceData.getNowVpnBean().country_name
    }

    private fun intIP() {
        showIpDialog = IpUtils.isIllegalIp()
        showIpAlertDialog()
        Log.e("TAG", "intIP: ${showIpDialog}")
    }

    @SuppressLint("RtlHardcoded")
    fun clickFUn() {
        binding.viewGuide1.setOnClickListener {}
        binding.linNav.setOnClickListener {
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            if (clickToast()) return@setOnClickListener
            jumpToWebActivity()
        }
        binding.imgVpn.setOnClickListener {
            clickVpn()
        }
        binding.lavGuide.setOnClickListener {
            clickVpn()
        }
        binding.imgX.setOnClickListener {
            cloneDialogFun()
        }
        binding.tvAuto.setOnClickListener {
            switchVpnModel(1)
        }
        binding.tvSs.setOnClickListener {
            switchVpnModel(2)
        }
        binding.tvOpen.setOnClickListener {
            switchVpnModel(3)
        }
        binding.imgXModel.setOnClickListener {
            binding.showModel = false
        }
        binding.tvModel.setOnClickListener {
            setVpnModel(mModelInt)
            clickVpn()
        }
        binding.imgSetting.setOnClickListener {
            clickSetting {
                if (clickToast()) return@clickSetting
                binding.dlMain.openDrawer(Gravity.LEFT)
            }
        }
        binding.constraintLayoutService.setOnClickListener {
            if (clickToast()) return@setOnClickListener
            jumpServiceListActivity()
        }
        onBackPressedDispatcher.addCallback {
            ClockUtils.ifAddThis("onBackPressedDispatcher") {}
            if (ClockUtils.complexLogicReturnsFalse(
                    listOf(2334, 2256), "onBackPressedDispatcher"
                )
            ) {
                return@addCallback
            }
            if (!ClockUtils.complexLogicAlwaysTrue("onBackPressedDispatcher")) {
                return@addCallback
            }
            if (binding?.vpnGuide == true) {
                binding.vpnGuide = false
                App.isShow = false
                return@addCallback
            }
            clickStopFun {
                finish()
            }
        }
    }

    private fun setVpnModel(model: Int) {
        App.vpnModel = model
        binding.vpnModel = App.vpnModel
        binding.showModel = false
    }

    private fun switchVpnModel(model: Int) {
        if (binding.vpnModel == model) {
            return
        }
        mModelInt = model
        if (App.getVpnState()) {
            binding.showModel = true
        } else {
            setVpnModel(mModelInt)
        }
    }

    private fun clickStopFun(nextFun: () -> Unit) {
        if (isConnectFun()) {
            Toast.makeText(this, "Connecting, will be completed shortly", Toast.LENGTH_SHORT).show()
            return
        }
        if (isDisConnectFun()) {
            Toast.makeText(this, "Disconnecting, will be completed shortly", Toast.LENGTH_SHORT)
                .show()
            return
        }
        nextFun()
    }

    private fun isDisConnectFun(): Boolean {
        return isDisConnect()
    }

    private fun isConnectFun(): Boolean {
        return isConnect()
    }

    private fun isDisConnect(): Boolean {
        return vpnState == 1 && beforeVpnState == 2
    }

    private fun isConnect(): Boolean {
        return vpnState == 1 && (beforeVpnState == 0 || beforeVpnState == -1)
    }

    private fun clickToast(): Boolean {
        val data = DishNomadicLoad.getMainClickData()
        if (data && App.isVpnState != 2 && !DataKeyUtils.autoConnect) {
            Toast.makeText(
                this,
                "VPN connection is required to use this feature. Connecting...",
                Toast.LENGTH_SHORT
            ).show()
            clickVpn()
            return true
        }
        return false
    }


    private fun showIpAlertDialog() {
        if (showIpDialog) {
            // 获取 IP 数据
            val ip = if (DataKeyUtils.ipData1.isEmpty()) {
                DataKeyUtils.ipData2
            } else {
                DataKeyUtils.ipData1
            }

            // 发送数据
            CanDataUtils.postPointData("antur3", "qu", ip)

            // 构建对话框
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Tips")
            builder.setMessage("Due to local laws and regulations, this service is not available in your country/region")

            // 设置确认按钮
            builder.setPositiveButton("confirm") { dialog, _ ->
                exitApp() // 执行退出应用的操作
                dialog.dismiss() // 关闭对话框
            }
            // 显示对话框
            val alertDialog = builder.create()
            alertDialog.setCancelable(false) // 禁用点击外部关闭对话框
            alertDialog.show()
            // 可选：自定义按钮样式
            val confirmButton: Button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            confirmButton.text = "confirm"
        }
    }


    private fun showSwitchDialogFun() {
        lifecycleScope.launch {
            if (App.isVpnState == 2 && !App.showSwitchState && !DishNomadicLoad.getBuyingShieldData() && DishNomadicLoad.showAdBlacklist() && DishNomadicLoad.getIntervalTimes()) {
                binding.showSwitch = true
                BaseAdLoad.interHaHaHaOPNNOPIN.preload(this@XmlMainActivity)
                CanDataUtils.postPointData(
                    "antur26",
                    "qu",
                    App.top_activity_Quan?.javaClass?.simpleName,
                )
                return@launch
            }
            if (App.isVpnState == 2 && App.showSwitchState && !App.connectSwitchState && !DishNomadicLoad.getBuyingShieldData() && DishNomadicLoad.showAdBlacklist()) {
                switchFun()
            }else{
                getHomeNativeAd()
            }
        }
    }

    private suspend fun switchFun() {
        App.connectSwitchState = true
        GetServiceData.setNowVpnBean(GetServiceData.getVpnSmartData())
        App.jumpSwitchState = true
        mService?.disconnect()
//        Core.stopService()
        delay(100)
        clickVpn()
    }


    private fun getHomeNativeAd() {
        Log.e("TAG", "getHomeNativeAd: ", )
        App.isAppRunning=false
        adJobDialogMain?.cancel()
        adJobDialogMain = null
        val endNav = BaseAdLoad.getMainNativeAdData()
        endNav.preload(this)
        adJobDialogMain = lifecycleScope.launch {
            delay(300)
            runCatching {
                while (true) {
                    Log.e("TAG", "showNativeAd: ${endNav.haveCache}")
                    if (endNav.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
                        endNav.showFullScreenAdBIUYBUI(this@XmlMainActivity) {
                            adJobDialogMain?.cancel()
                            adJobDialogMain = null
                        }
                        break
                    }
                    delay(500)
                }
            }
        }
    }


    private fun clickSetting(nextFun: () -> Unit) {
        clickStopFun {
            nextFun()
        }
    }

    private fun showConnectAdTime(seconds: Int = 20, nextFun: () -> Unit) {
        if ((!DishNomadicLoad.showAdBlacklist()) || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        jobConnect?.cancel()
        jobConnect = null
        val max = seconds * 10
        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
        jobConnect = GetServiceData.countDown(max, 100, MainScope(), {
            if (it > 20) {
                BaseAdLoad.showConnectAdIfCan(this@XmlMainActivity) {
                    Log.e("TAG", "showConnectAdTime: ${this.lifecycle.currentState}")
                    if (this.lifecycle.currentState == Lifecycle.State.RESUMED || this.lifecycle.currentState == Lifecycle.State.STARTED) {
                        nextFun()
                    }

                }
            }
        }, {
            nextFun()
        })
    }

    private fun showCloneDialogAd(seconds: Int = 3, nextFun: () -> Unit) {
        if ((!DishNomadicLoad.showAdBlacklist()) || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        jobConnect?.cancel()
        jobConnect = null
        val max = seconds * 10
        jobConnect = GetServiceData.countDown(max, 100, MainScope(), {
            if (it > 10) {
                BaseAdLoad.showConnectAdIfCan(this@XmlMainActivity) {
                    if (this.lifecycle.currentState == Lifecycle.State.RESUMED || this.lifecycle.currentState == Lifecycle.State.STARTED) {
                        nextFun()
                    }
                }
            }
        }, {
            nextFun()
        })
    }

    private fun cloneDialogFun() {
        lifecycleScope.launch {
            CanDataUtils.postPointData(
                "antur28",
                "qu",
                App.top_activity_Quan?.javaClass?.simpleName,
            )
            binding.showIntAd = true
            showCloneDialogAd {
                binding.showIntAd = false
                binding.showSwitch = false
            }
        }
    }

    private fun clickVpn() {
        App.isShow = false
        binding.vpnGuide = false
        if (vpnState == 1) {
            return
        }
        IpUtils.getIfConfig()
        intIP()
        if (showIpDialog) {
            return
        }
        if (App.isVpnState != 2) {
            CanDataUtils.postPointData("antur6")
        }
        if (!GetServiceData.isHaveNetWork(this)) {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show()
            return
        }
        if (!checkVPNPermission(this@XmlMainActivity)) {
            if (!DataKeyUtils.service_q_x_type) {
                DataKeyUtils.service_q_x_type = true
                CanDataUtils.postPointData("antur7")
            }
            VpnService.prepare(this)?.let(requestPermissionForResultVPN::launch)
            return
        }
        connectVpnStateFun {
            beforeVpnState = vpnState
            connecting()
            DataKeyUtils.autoConnect = true
            Log.e("TAG", "clickVpn: beforeVpnState=${beforeVpnState}")
            App.jumpSwitchState = false
            if (beforeVpnState == 0 || beforeVpnState == -1) {
                setVpnInfor()
                setUpConfigConnections(this@XmlMainActivity)
                return@connectVpnStateFun
            }
            showConnectAdTime(DishNomadicLoad.parseTwoNumbers().second) {
                Log.e("TAG", "clickVpn: stopService1")
//                Core.stopService()
                mService?.let {
                    setVpnInfor()
                    lifecycleScope.launch {
                        if (beforeVpnState == 2) {
                            Log.e("TAG", "clickVpn: stopService2")
                            it.disconnect()
                            CanDataUtils.postPointData("antur13")
                        }
                    }
                }
            }
        }
    }

    private fun connectVpnStateFun(nextFun: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {

            if (GetServiceData.isHaveServeData()) {
                withContext(Dispatchers.Main) {
                    binding.showIntAd = false
                    nextFun()
                }
            } else {
                binding.showIntAd = true
                delay(2000)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@XmlMainActivity, "No data yet", Toast.LENGTH_SHORT).show()
                    binding.showIntAd = false
                }
            }
        }
    }

    fun connectSuccess() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.vpnGuide = false
            App.isVpnState = 2
            binding.imgVpn.stopRotate()
            timerViewModel.startTimer()
            binding.imgVpn.setImageResource(R.drawable.ic_vpn_3)
            Log.e("TAG", "connectSuccess: $vpnState")
            jumpResultActivity()
            vpnState = 2
            binding.tvVpnState.text = getConcatenateText()
        }
    }

    private fun connecting() {
        binding.imgVpn.setImageResource(R.drawable.ic_vpn_2)
        binding.imgVpn.startRotate()
        vpnState = 1
        binding.tvVpnState.text = getConcatenateText()
    }

    fun disConnectSuccess() {
        lifecycleScope.launch(Dispatchers.Main) {
            App.isVpnState = 0
            binding.imgVpn.setImageResource(R.drawable.ic_vpn_1)
            binding.imgVpn.stopRotate()
            timerViewModel.stopTimer()
            Log.e("TAG", "disConnectSuccess: $vpnState===${beforeVpnState}")
            jumpResultActivity()
            vpnState = 0
            binding.tvVpnState.text = getConcatenateText()
        }
    }

    fun connectFail() {
        if (App.isVpnState != 2) {
            vpnState = -1
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(this@XmlMainActivity, "Connection failed", Toast.LENGTH_SHORT).show()
            }
            mService?.disconnect()
        }
    }

    private fun jumpResultActivity() {
        if (App.jumpSwitchState) return
        lifecycleScope.launch {
            delay(200)
            if (beforeVpnState != -2 && this@XmlMainActivity.lifecycle.currentState == Lifecycle.State.RESUMED) {
                val intent = Intent(this@XmlMainActivity, XmlResultActivity::class.java)
                startActivityForResult(intent, 0x445)
            }
        }
    }

    private fun jumpServiceListActivity() {
        clickStopFun {
            isHaveServeData {
                val intent = Intent(this, ServiceListActivity::class.java)
                startActivityForResult(intent, 0x334)
            }
        }
    }

    private fun isHaveServeData(nextFun: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (GetServiceData.isHaveServeData()) {
                withContext(Dispatchers.Main) {
                    binding.showIntAd = false
                }
            } else {
                binding.showIntAd = true
                delay(2000)
                withContext(Dispatchers.Main) {
                    binding.showIntAd = false
                }
            }
            withContext(Dispatchers.Main) {
                nextFun()
            }
        }
    }

    private fun jumpToWebActivity() {
        val intent = Intent(this, WebActivity::class.java)
        startActivityForResult(intent, 0x555)
    }

    private fun exitApp() {
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun showStateFalse() {
        App.showSwitchState = false
        App.connectSwitchState = false
    }

    private fun requestPermissionForResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            clickVpn()
            showStateFalse()
            if (!DataKeyUtils.service_q_x_type2) {
                DataKeyUtils.service_q_x_type2 = true
                CanDataUtils.postPointData("antur8")
            }
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName?,
            service: IBinder?,
        ) {
            mService = IOpenVPNAPIService.Stub.asInterface(service)
            try {
                mService?.registerStatusCallback(mCallback)
            } catch (e: Exception) {
            }
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            mService = null
        }
    }

    private fun checkVPNPermission(ac: Activity): Boolean {
        VpnService.prepare(ac).let {
            return it == null
        }
    }

//    private fun setSkServerData(profile: Profile, serverVpn: ServerVpn): Profile {
//        Log.e("TAG", "setSkServerData: ip=${serverVpn.ip}")
//        profile.name = serverVpn.country_name + "-" + serverVpn.city
//        profile.host = serverVpn.ip
//        profile.password = serverVpn.user_pwd
//        profile.method = "chacha20-ietf-poly1305"
//        profile.remotePort = serverVpn.port
//        return profile
//    }

    private fun setUpConfigConnections(context: Context) {
        val vpnData = GetServiceData.getNowVpnBean()
        DataKeyUtils.tba_vpn_ip = vpnData.ip
        DataKeyUtils.tba_vpn_city = vpnData.city
//        if (binding.vpnModel != 3) {
//            Log.e("TAG", "setUpConfigConnections1: $vpnData")
//            if (App.getVpnState()) {
//                Log.e("TAG", "setUpConfigConnections2: $vpnData")
//                mService?.disconnect()
//            }
//            ProfileManager.getProfile(DataStore.profileId).let {
//                if (it != null) {
//                    ProfileManager.updateProfile(setSkServerData(it, vpnData))
//                } else {
//                    val profile = Profile()
//                    ProfileManager.createProfile(setSkServerData(profile, vpnData))
//                }
//            }
//            DataStore.profileId = 1L
//            Core.startService()
//            return
//        }
        MainScope().launch(Dispatchers.IO) {
            runCatching {
//                if (App.getVpnState()) {
//                    Core.stopService()
//                }
                CanDataUtils.postPointData("antur9")
                context.assets.open("fast_ippooltest.ovpn").bufferedReader().use { br ->
                    val config = br.lineSequence().map { line ->
                        when {
                            line.contains(
                                "remote 195", ignoreCase = true
                            ) -> "remote ${vpnData.ip} ${vpnData.port}"

                            line.contains(
                                "wrongpassword", ignoreCase = true
                            ) -> vpnData.user_pwd

                            line.contains(
                                "cipher AES-256-GCM", ignoreCase = true
                            ) -> "cipher ${vpnData.mode}"

                            else -> line
                        }
                    }.joinToString("\n")
                    Log.e("TAG", "openVTool: $config")
                    if (vpnState == 1) mService?.startVPN(config)
                }
            }.onFailure {
                // Handle failure
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("TAG", "onActivityResult11111: $requestCode")

        if (requestCode == 0x334 && DataKeyUtils.selectVpnServiceData != "" && vpnState != 2) {
            DataKeyUtils.nowVpnServiceData = DataKeyUtils.selectVpnServiceData
            setVpnInfor()
            DataKeyUtils.selectVpnServiceData = ""
            clickVpn()
            showStateFalse()
        }
        if (requestCode == 0x334 && DataKeyUtils.selectVpnServiceData != "" && vpnState == 2) {
            clickVpn()
            showStateFalse()
        }
        if (requestCode == 0x445 && DataKeyUtils.selectVpnServiceData != "" && vpnState != 2) {
            DataKeyUtils.nowVpnServiceData = DataKeyUtils.selectVpnServiceData
            setVpnInfor()
            DataKeyUtils.selectVpnServiceData = ""
        }
        if (requestCode == 0x445) {
            Log.e("TAG", "onActivityResult: $requestCode")
            showSwitchDialogFun()
        }
        if (requestCode == 0x334 || requestCode == 0x555) {
            getHomeNativeAd()
        }
    }

    fun getConcatenateText(): String {
        var text = ""
        if (vpnState == 2) {
            text = "Connected"
        }
        if (vpnState == 0 || vpnState == -1) {
            text = "DisConnected"
        }
        if (isDisConnect()) {
            text = "DisConnecting"
        }
        if (isConnect()) {
            text = "Connecting"
        }
        return text

    }

    override fun onStart() {
        super.onStart()
//        connection.bandwidthTimeout = 500
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(100)
            if(this@XmlMainActivity.isActivityResumed()){
                if (App.isAppRunning) {
                    getHomeNativeAd()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
//        connection.bandwidthTimeout = 0
//        connection.disconnect(this)
        Log.e("TAG", "onStop: ")
        jobConnect?.cancel()
        jobConnect = null
        if (isDisConnect()) {
            vpnState = -1
            connecting()
        }
        if (isConnect()) {
            vpnState = -1
            disConnectSuccess()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "main--------onDestroy: ")
//        DataStore.publicStore.unregisterChangeListener(this)
//        connection.disconnect(this)
        try {
            this.unbindService(mConnection)
        } catch (e: Exception) {
        }
        adJobDialogMain?.cancel()
        adJobDialogMain = null
    }

//    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
//        Log.e("TAG", "vpnState-ss: ${state.name}-beforeVpnState=${beforeVpnState}")
//        if (state.name == "Connected") {
//            connectFun()
//        }
//        if (state.name == "Stopped") {
//            disConnectFun()
//        }
//    }
//
//    override fun onServiceConnected(service: IShadowsocksService) {
//        if (App.vpnModel == 3) {
//            return
//        }
//        val state = BaseService.State.values()[service.state]
//        Log.e("TAG", "vpnState-ss-c: ${state.name}-beforeVpnState=${beforeVpnState}")
//        if (state.name == "Connected") {
//            connectFun()
//        }
//        if (state.name == "Stopped") {
//            disConnectFun()
//        }
//    }
//
//    override fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String) {
//        when (key) {
//            Key.serviceMode -> {
//                connection.disconnect(this)
//                connection.connect(this, this)
//            }
//        }
//    }

    private val mCallback = object : IOpenVPNStatusCallback.Stub() {
        override fun newStatus(uuid: String?, state: String?, message: String?, level: String?) {
            Log.e(
                "TAG",
                "vpnState-open: ${state}-beforeVpnState=${beforeVpnState}-App.vpnModel=${App.vpnModel}"
            )
//            if (App.vpnModel != 3) {
//                return
//            }
            if (state == "CONNECTED") {
                connectFun()
            }
            if (state == "RECONNECTING") {
                connectFail()
                BaseAdLoad.getEndNativeAdData().preload(this@XmlMainActivity)
                CanDataUtils.postPointData("antur11")
                beforeVpnState = -2
                showStateFalse()
            }
            if (state == "NOPROCESS") {
                disConnectFun()
            }
        }
    }

    private fun connectFun() {
        if (beforeVpnState == -2) {
            connectSuccess()
            return
        }
        if (DataKeyUtils.firstDialogState) {
            BaseAdLoad.getInterResultAdData().preload(this@XmlMainActivity)
            BaseAdLoad.getMainNativeAdData().preload(this@XmlMainActivity)
            BaseAdLoad.getEndNativeAdData().preload(this@XmlMainActivity)
        }
        showConnectAdTime(DishNomadicLoad.parseTwoNumbers().first) {
            connectSuccess()
            BaseAdLoad.interHaHaHaOPNNOPIN.preload(this@XmlMainActivity)
            BaseAdLoad.getInterResultAdData().preload(this@XmlMainActivity)
            BaseAdLoad.getMainNativeAdData().preload(this@XmlMainActivity)
        }
        CanDataUtils.antur10()
    }

    private fun disConnectFun() {
        disConnectSuccess()
        if (beforeVpnState == 2) {
            BaseAdLoad.getInterResultAdData().preload(this@XmlMainActivity)
            BaseAdLoad.getEndNativeAdData().preload(this@XmlMainActivity)
        }
    }
}