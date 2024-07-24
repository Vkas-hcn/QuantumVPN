package com.bee.open.ant.fast.composeopen.ui.main

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.ui.theme.black
import com.bee.open.ant.fast.composeopen.ui.theme.guojia
import com.bee.open.ant.fast.composeopen.ui.theme.white
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme
import de.blinkt.openvpn.api.ExternalOpenVPNService
import de.blinkt.openvpn.api.IOpenVPNAPIService
import de.blinkt.openvpn.api.IOpenVPNStatusCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.net.IpUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.ui.end.ResultActivity
import com.bee.open.ant.fast.composeopen.ui.service.ServiceListActivity
import androidx.compose.material.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.ui.web.WebActivity
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    var mService: IOpenVPNAPIService? = null
    lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>
    var showProgress by mutableStateOf(false)
    var isRotating by mutableStateOf(false)
    var vpnState by mutableStateOf(-1)  // 0 未连接 1 连接中 2 已连接 -1 未知
    var beforeVpnState by mutableStateOf(-2)
    var showIpDialog by mutableStateOf(false)
    var showNoTiFaDialog by mutableStateOf(false)

    var countryName by mutableStateOf("")
    var city by mutableStateOf("")
    var ip by mutableStateOf("")
    var jobConnect: Job? = null
    var showIntAd by mutableStateOf(false)

    var showNavAd by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (App.isShow) {
                App.isShow = false
            } else {
                clickStopFun {
                    finish()
                }
            }
        }
        setContent {
            QuantumVpnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    DrawerExample(this@MainActivity)

                }
            }
        }
        intIP()
        initData()
        try {
            this.bindService(
                Intent(this, ExternalOpenVPNService::class.java), mConnection, BIND_AUTO_CREATE
            )
        } catch (e: Exception) {
            Log.e("TAG", "bindService: $e")
        }
        DishNomadicLoad.getSpoilerData()
        requestPermissionForResultVPN =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                requestPermissionForResult(it)
            }
        showNaAd()

    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                showNoTiFaDialog = true
            } else {
                CanDataUtils.postPointData("antur4")
                clickVpn()
            }
        }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(300)
            CanDataUtils.postPointData("antur2")
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && App.isAppRunning) {
                BaseAdLoad.mainNativeHome.showFullScreenAdBIUYBUI(this@MainActivity) {}
                App.isAppRunning = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            this.unbindService(mConnection)
        } catch (e: Exception) {
        }
    }

    private fun showNaAd() {
        lifecycleScope.launch {
            while (isActive) {
                if (App.appNativeAdHome == null) {
                    BaseAdLoad.mainNativeHome.showFullScreenAdBIUYBUI(this@MainActivity) {}
                } else {
                    cancel()
                }
                delay(500)
            }
        }
    }


    private fun initData() {
        countryName = GetServiceData.getNowVpnBean().country_name
        city = GetServiceData.getNowVpnBean().city
        ip = GetServiceData.getNowVpnBean().ip
    }

    fun intIP() {
        showIpDialog = IpUtils.isIllegalIp()
        Log.e("TAG", "intIP: ${showIpDialog}")
    }

    fun clickSetting(nextFun: () -> Unit) {
        clickStopFun {
            nextFun()
        }
    }

    private fun showConnectAdTime(nextFun: () -> Unit) {
        if ((!DishNomadicLoad.showAdBlacklist()) || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        jobConnect?.cancel()
        jobConnect = null
        jobConnect = GetServiceData.countDown(100, 100, MainScope(), {
            if (it > 20) {
                BaseAdLoad.showConnectAdIfCan(this@MainActivity) {
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

    fun clickVpn() {
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
        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
        BaseAdLoad.interHaHaHaOPNNOPIN2.preload(this)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU && App.isVpnState != 2) {
            CanDataUtils.postPointData("antur5")
        }
        if (!checkVPNPermission(this@MainActivity)) {
            if (!DataKeyUtils.service_q_x_type) {
                DataKeyUtils.service_q_x_type = true
                CanDataUtils.postPointData("antur7")
            }
            VpnService.prepare(this)?.let(requestPermissionForResultVPN::launch)
            return
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
                Toast.makeText(
                    this,
                    "Android 14 devices require notification permissions for VPN service usage",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

        }
        connectVpnStateFun {
            beforeVpnState = vpnState
            connecting()
            showConnectAdTime {
                mService?.let {
                    initData()
                    if (beforeVpnState == 0 || beforeVpnState == -1) {
                        setUpConfigConnections(this@MainActivity)
                    }
                    if (beforeVpnState == 2) {
                        BaseAdLoad.mainNativeEnd.preload(this@MainActivity)
                        Log.e("TAG", "clickVpn: 断开")
                        it.disconnect()
                        CanDataUtils.postPointData("antur13")
                    }
                }
            }
        }
    }

    private fun connectVpnStateFun(nextFun: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgress = true
            }
            if (GetServiceData.isHaveServeData()) {
                withContext(Dispatchers.Main) {
                    showProgress = false
                    nextFun()
                }
            } else {
                delay(2000)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "No data yet", Toast.LENGTH_SHORT).show()
                    showProgress = false
                }
            }
        }
    }

    fun connectSuccess() {
        App.isVpnState = 2
        isRotating = false
        Log.e("TAG", "connectSuccess: $vpnState")
        jumpResultActivity()
        vpnState = 2
    }

    fun connecting() {
        isRotating = true
        vpnState = 1
        App.isVpnState = 1
    }

    fun disConnectSuccess() {
        App.isVpnState = 0
        isRotating = false
        Log.e("TAG", "disConnectSuccess: $vpnState")
        jumpResultActivity()
        vpnState = 0
    }

    fun connectFail() {
        if (App.isVpnState != 2) {
            vpnState = -1
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Connection failed", Toast.LENGTH_SHORT).show()
            }
            mService?.disconnect()
        }
    }

    private fun jumpResultActivity() {
        if (beforeVpnState != -2) {
            val intent = Intent(this, ResultActivity::class.java)
            startActivityForResult(intent, 0x445)
        }
    }

    fun jumpServiceListActivity() {
        clickStopFun {
            isHaveServeData {
                val intent = Intent(this, ServiceListActivity::class.java)
                startActivityForResult(intent, 0x334)
            }
        }
    }

    private fun isHaveServeData(nextFun: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgress = true
            }
            if (GetServiceData.isHaveServeData()) {
                withContext(Dispatchers.Main) {
                    showProgress = false
                }
            } else {
                delay(2000)
                withContext(Dispatchers.Main) {
                    showProgress = false
                }
            }
            withContext(Dispatchers.Main) {
                nextFun()
            }
        }
    }

    fun jumpToWebActivity() {
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)
    }

    fun exitApp() {
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun requestPermissionForResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            clickVpn()
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
    private val mCallback = object : IOpenVPNStatusCallback.Stub() {
        override fun newStatus(uuid: String?, state: String?, message: String?, level: String?) {
            Log.e("TAG", "newStatus: ${state}")
            if (state == "CONNECTED") {
                connectSuccess()
                BaseAdLoad.mainNativeEnd.preload(this@MainActivity)
                CanDataUtils.antur10()

            }
            if (state == "RECONNECTING") {
                connectFail()
                BaseAdLoad.mainNativeEnd.preload(this@MainActivity)
                CanDataUtils.postPointData("antur11")
                beforeVpnState = -2
            }
            if (state == "NOPROCESS") {
                disConnectSuccess()
            }
        }
    }

    private fun checkVPNPermission(ac: Activity): Boolean {
        VpnService.prepare(ac).let {
            return it == null
        }
    }

    private fun setUpConfigConnections(context: Context): Job? = with(context) {
        return MainScope().launch(Dispatchers.IO) {
            runCatching {
                CanDataUtils.postPointData("antur9")
                val vpnData = GetServiceData.getNowVpnBean()
                DataKeyUtils.tba_vpn_ip = vpnData.ip
                DataKeyUtils.tba_vpn_city = vpnData.city
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
        if (requestCode == 0x334 && DataKeyUtils.selectVpnServiceData != "" && vpnState != 2) {
            DataKeyUtils.nowVpnServiceData = DataKeyUtils.selectVpnServiceData
            countryName = GetServiceData.getNowVpnBean().country_name
            city = GetServiceData.getNowVpnBean().city
            ip = GetServiceData.getNowVpnBean().ip
            DataKeyUtils.selectVpnServiceData = ""
            clickVpn()
        }
        if (requestCode == 0x334 && DataKeyUtils.selectVpnServiceData != "" && vpnState == 2) {
            clickVpn()
        }
        if (requestCode == 0x445 && DataKeyUtils.selectVpnServiceData != "" && vpnState != 2) {
            DataKeyUtils.nowVpnServiceData = DataKeyUtils.selectVpnServiceData
            countryName = GetServiceData.getNowVpnBean().country_name
            city = GetServiceData.getNowVpnBean().city
            ip = GetServiceData.getNowVpnBean().ip
            DataKeyUtils.selectVpnServiceData = ""
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

    private fun isDisConnect(): Boolean {
        return vpnState == 1 && beforeVpnState == 2
    }

    private fun isConnect(): Boolean {
        return vpnState == 1 && (beforeVpnState == 0 || beforeVpnState == -1)
    }

    private fun isDisConnectFun(): Boolean {
        if (isDisConnect()) {
            jobConnect?.cancel()
            jobConnect = null
            vpnState = -1
            connectSuccess()
            return true
        }
        return false
    }

    private fun isConnectFun(): Boolean {
        return isConnect()
    }

    private fun clickStopFun(nextFun: () -> Unit) {
        if (isDisConnectFun()) {
            return
        }
        if (isConnectFun()) {
            Toast.makeText(this, "Please wait a while while connecting", Toast.LENGTH_SHORT).show()
            return
        }
        nextFun()
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "onStop: ")
        jobConnect?.cancel()
        jobConnect = null
        if (isDisConnect()) {
            //断开过程中
            vpnState = -1
            connectSuccess()
        }
        if (isConnect()) {
            //连接过程中
            vpnState = -1
            disConnectSuccess()
        }
    }
}

@Composable
fun BoxWithProgress(activity: MainActivity) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .background(Color(0x00000000))
            .fillMaxSize()
    ) {

        if (activity.showProgress) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .clickable {

                })
        }
    }
}

@Composable
fun LottieImageAnimation(activity: MainActivity) {
    val composition = rememberLottieComposition(LottieCompositionSpec.Asset(assetName = "data.zip"))
    if (App.isShow && App.isVpnState != 2) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .background(Color(0xB3000000))) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 250.dp, start = 80.dp)
                    .fillMaxWidth()
            ) {
                LottieAnimation(composition = composition.value,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                        .clickable {
                            activity.clickVpn()
                            App.isShow = false
                        })
            }
        }
    }

}

@Composable
fun loadVpnData(activity: MainActivity) {
    BoxWithProgress(activity)
    LottieImageAnimation(activity)
}

@Composable
fun RotatingImageWithControl(
    activity: MainActivity, timerViewModel: TimerViewModel = viewModel()
) {
    var rotationDegrees by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(activity.isRotating) {
        while (activity.isRotating) {
            delay(16)
            rotationDegrees += 5f
            if (rotationDegrees >= 360f) {
                rotationDegrees = 0f
            }
        }
    }

    Column {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 46.dp)
                .requiredSize(192.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {
                    activity.clickVpn()
                }

        ) {
            Image(painter = painterResource(
                id = when (activity.vpnState) {
                    0, -1 -> {
                        timerViewModel.stopTimer()
                        R.drawable.ic_vpn_1
                    }

                    1 -> R.drawable.ic_vpn_2
                    2 -> {
                        if (activity.beforeVpnState != activity.vpnState) {
                            timerViewModel.startTimer()
                        }
                        R.drawable.ic_vpn_3
                    }

                    else -> R.drawable.ic_vpn_1
                }
            ), contentDescription = "Background", modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = rotationDegrees
                })
            Image(
                painter = painterResource(
                    id = if (activity.vpnState == 2) {
                        R.drawable.ic_sw_2
                    } else {
                        R.drawable.ic_sw_1
                    }
                ),
                contentDescription = "Background",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(48.dp)
                    .padding()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TimerScreen(timerViewModel: TimerViewModel) {
    val time by timerViewModel.timerText.collectAsState()
    Column {
        Text(
            text = time, fontSize = 15.sp, color = black, modifier = Modifier
        )
    }
}

@Composable
fun ArtistCardRow(activity: MainActivity, clickSettingFun: () -> Unit) {
    val timerViewModel: TimerViewModel = viewModel()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF4F4F4))
        .clickable {

        }) {
        Image(
            painter = painterResource(id = R.drawable.bg_main_2),
            contentScale = ContentScale.Crop,
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 82.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bg_main_1),
            contentScale = ContentScale.Crop,
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
        Image(
            painter = painterResource(id = R.drawable.bg_main_3),
            contentScale = ContentScale.Crop,
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
        Row {
            Image(painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "Background",
                modifier = Modifier
                    .padding(top = 120.dp)
                    .requiredSize(56.dp)
                    .clickable {
                        activity.clickSetting {
                            clickSettingFun()
                        }
                    })
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimerScreen(timerViewModel)
                Text(
                    text = GetServiceData.getNowVpnBean().country_name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = guojia,
                    modifier = Modifier
                )

                Text(
                    text = activity.getConcatenateText(),
                    fontSize = 15.sp,
                    color = black,
                    modifier = Modifier
                )
                RotatingImageWithControl(activity, timerViewModel)
            }
            Image(painter = painterResource(id = R.drawable.ic_click),
                contentDescription = "Background",
                modifier = Modifier
                    .alpha(0f)
                    .padding(top = 120.dp)
                    .requiredSize(56.dp)
                    .clickable {
//                        Toast
//                            .makeText(
//                                activity, "The feature is not available yet", Toast.LENGTH_SHORT
//                            )
//                            .show()
                    })
        }

        BoxWithConstraints(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxWidth()
                .height(71.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_service),
                contentScale = ContentScale.Crop,
                contentDescription = "Background",
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp)
                    .clickable {
                        activity.jumpServiceListActivity()
                    }) {
                Image(
                    painter = painterResource(id = GetServiceData.getCountryFlag(activity.countryName)),
                    contentDescription = "Artist image",
                    modifier = Modifier.requiredSize(28.dp)
                )
                Spacer(modifier = Modifier.width(13.dp))
                Column {
                    Text(
                        text = "${activity.countryName},${activity.city}",
                        fontSize = 16.sp,
                        color = white,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        "IP：${activity.ip}",
                        fontSize = 12.sp,
                        color = white,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            activity.showNavAd = !DishNomadicLoad.showAdBlacklist()
            if (!activity.showNavAd) {
                if(!BaseAdLoad.canShowAD()){
                    App.appNativeAdHome != null
                }
                if (App.appNativeAdHome != null) {
                    NativeAdHomeContent(App.appNativeAdHome!!)
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ad_bg),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }

        }
        LoadingDialog(activity)
        loadVpnData(activity)
    }
}

@Composable
fun IpAlertDialog(activity: MainActivity) {
    if (activity.showIpDialog) {
        val ip = DataKeyUtils.ipData1.ifEmpty {
            DataKeyUtils.ipData2
        }
        CanDataUtils.postPointData("antur3", "qu", ip)
        AlertDialog(onDismissRequest = {}, title = {
            Text(text = "Tips")
        }, text = {
            Text("Due to local laws and regulations, this service is not available in your country/region")
        }, confirmButton = {
            Button(onClick = {
                activity.exitApp()
            }) {
                Text("confirm")
            }
        })
    }
}

@Composable
fun notificationAlertDialog(activity: MainActivity) {
    if (activity.showNoTiFaDialog) {
        AlertDialog(onDismissRequest = {}, title = {
            Text(text = "Tips")
        }, text = {
            Text("Notification permission denied. Please enable it in settings.")
        }, confirmButton = {
            Button(onClick = {
                activity.openAppSettings()
            }) {
                Text("confirm")
            }
        }, dismissButton = {
            Button(onClick = {
                activity.showNoTiFaDialog = false
            }) {
                Text("dismiss")
            }
        })
    }
}

@Composable
fun DrawerExample(activity: MainActivity) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val drawerWidth = screenWidth * 3 / 4
    ModalDrawer(drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerBackgroundColor = Color(0xFF1A6956),
        drawerContent = {
            Column(
                modifier = Modifier
                    .width(drawerWidth)
                    .fillMaxSize()
                    .background(Color(0xFF1A6956)),

                ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_start_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(top = 82.dp, start = 20.dp)
                        .requiredSize(82.dp)
                )

                Text("Privacy Policy",
                    fontSize = 15.sp,
                    color = white,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF1E7A64))
                        .padding(20.dp)
                        .clickable {
                            activity.jumpToWebActivity()
                        })
            }
        }) {
        ArtistCardRow(activity) {
            scope.launch {
                drawerState.open()
            }
        }
        IpAlertDialog(activity)
        notificationAlertDialog(activity)
    }
}

@Composable
fun NativeAdHomeContent(nativeAd: NativeAd) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        factory = { context ->
            val nativeAdView = NativeAdView(context)
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.layout_main, null) as NativeAdView
            nativeAdView.addView(adView)
            adView.findViewById<TextView>(R.id.ad_headline).text = nativeAd.headline
            adView.findViewById<TextView>(R.id.ad_body).text = nativeAd.body
            adView.findViewById<ImageView>(R.id.ad_app_icon)
                .setImageDrawable(nativeAd.icon?.drawable)
            adView.findViewById<TextView>(R.id.ad_call_to_action).text = nativeAd.callToAction
            Log.e("TAG", "首页原生广告展示:")
            adView.setNativeAd(nativeAd)
            nativeAdView
        }
    )
}

@Composable
fun LoadingDialog(activity: MainActivity) {
    if (activity.showIntAd) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {}
            .background(Color(0xB3000000))) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = Color.Black)
                    Text(
                        text = "Ads will show soon",
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    ArtistCardRow(activity = MainActivity(), {})
//    RotatingImageWithControl(activity = MainActivity())
    QuantumVpnTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            DrawerExample(activity = MainActivity())

        }
    }
}