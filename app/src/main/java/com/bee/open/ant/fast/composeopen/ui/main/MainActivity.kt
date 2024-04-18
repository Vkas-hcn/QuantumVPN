package com.bee.open.ant.fast.composeopen.ui.main

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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

import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.Lifecycle
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.ui.web.WebActivity

class MainActivity : ComponentActivity() {
    var mService: IOpenVPNAPIService? = null
    lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>
    var showProgress by mutableStateOf(false)
    var isRotating by mutableStateOf(false)
    var vpnState by mutableStateOf(-1)  // 0 未连接 1 连接中 2 已连接 -1 未知
    var beforeVpnState by mutableStateOf(0)
    var showIpDialog by mutableStateOf(false)
    var countryName by mutableStateOf("")
    var city by mutableStateOf("")
    var ip by mutableStateOf("")
    var jobConnect: Job? = null
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
        if (!GetServiceData.isHaveNetWork(this)) {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show()
            return
        }
        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
        BaseAdLoad.interHaHaHaOPNNOPIN2.preload(this)

        if (!checkVPNPermission(this@MainActivity)) {
            VpnService.prepare(this)?.let(requestPermissionForResultVPN::launch)
            return
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
                        Log.e("TAG", "clickVpn: 断开")
                        it.disconnect()
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
        Log.e("TAG", "connectSuccess: $vpnState", )
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
        Log.e("TAG", "disConnectSuccess: $vpnState", )
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
        if (vpnState != -1) {
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
            if (state == "CONNECTED" && vpnState !=-1) {
                connectSuccess()
            }
            if (state == "RECONNECTING") {
                connectFail()
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
                val vpnData = GetServiceData.getNowVpnBean()
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
            ), contentDescription = "Background", modifier = Modifier.graphicsLayer {
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
                    .padding(end = 12.dp, bottom = 22.dp, start = 8.dp)
                    .requiredSize(48.dp)
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
            if (false) {
                Image(painter = painterResource(id = R.drawable.ic_click),
                    contentDescription = "Background",
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .requiredSize(56.dp)
                        .clickable {
                            Toast
                                .makeText(
                                    activity, "The feature is not available yet", Toast.LENGTH_SHORT
                                )
                                .show()
                        })
            }

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
        loadVpnData(activity)

    }

}

@Composable
fun IpAlertDialog(activity: MainActivity) {
    if (activity.showIpDialog) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArtistCardRow(activity = MainActivity(), {})
}