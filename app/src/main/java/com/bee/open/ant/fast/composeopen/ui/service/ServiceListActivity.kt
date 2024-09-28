package com.bee.open.ant.fast.composeopen.ui.service

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.data.ServerVpn
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ServiceListActivity : ComponentActivity() {
    var showDialog by mutableStateOf(false)
    lateinit var checkServerVpn: ServerVpn
    var showIntAd by mutableStateOf(false)
    private var listBackJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumVpnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1B6A56)
                ) {
                    ServiceListView(this@ServiceListActivity)
                    LoadingDialog(this)
                }
            }
        }
        BaseAdLoad.getInterListAdData().preload(this)
        CanDataUtils.postPointData("antur18")
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
            backFun()
        }
    }

    fun backFun() {
        CanDataUtils.postPointData("antur20")
        showInt2Ad {
            finish()
        }
    }

    private fun showInt2Ad(nextFun: () -> Unit) {
        if (!DishNomadicLoad.showAdBlacklist() || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        val inter = BaseAdLoad.getInterListAdData()
        inter.preload(this)
        listBackJob?.cancel()
        listBackJob = null
        val max = 5 * 10
        showIntAd = true
        listBackJob = GetServiceData.countDown(max, 100, MainScope(), {
            if (inter.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
                lifecycleScope.launch(Dispatchers.Main) {
                    showIntAd = false
                    inter.showFullScreenAdBIUYBUI(this@ServiceListActivity) {
                        nextFun()
                    }
                }
            }
        }, {
            nextFun()
        })
    }

    fun getVpnServiceData(): MutableList<ServerVpn> {
        return GetServiceData.getAllVpnListData()
    }

    private fun isConnect(index: Int): Boolean {
        val serverVpn = getVpnServiceData()[index]
        if (App.isVpnState == 2) {
            if (GetServiceData.getNowVpnBean().isBest && index == 0) {
                return true
            }
            if (!GetServiceData.getNowVpnBean().isBest && index != 0) {
                return serverVpn.ip == GetServiceData.getNowVpnBean().ip
            }
            return false
        } else {
            return false
        }
    }

    fun showWhetherCheckImg(index: Int): Int {
        return if (isConnect(index)) {
            R.drawable.ic_cheack
        } else {
            R.drawable.ic_discheak
        }
    }

    fun selectTheServer(serverVpn: ServerVpn) {
        checkServerVpn = serverVpn
        if (App.isVpnState == 2) {
            if (serverVpn.ip == GetServiceData.getNowVpnBean().ip && serverVpn.isBest == GetServiceData.getNowVpnBean().isBest) {
                return
            }
            showDialog = true
        } else {
            backToMain()
        }
    }

    fun backToMain() {
        GetServiceData.setSelectVpnServiceData(checkServerVpn)
        finish()
    }
}

@Composable
fun ServiceListView(activity: ServiceListActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .requiredSize(24.dp)
                    .clickable {
                        activity.backFun()
                    },
            )
            Text(
                text = "Connect Report",
                color = Color.White,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding()
                    .weight(1f),
            )
            Spacer(Modifier.width(24.dp))
        }
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp),
            ) {
                Text(
                    text = "Server option",
                    color = Color.White,
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(start = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
                Text(
                    text = "Optimal location",
                    color = Color(0xFFADE2C2),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )

            }

            Image(
                painter = painterResource(id = R.drawable.ic_list_top),
                contentDescription = "Connect Success",
                modifier = Modifier
                    .padding()
                    .requiredSize(149.dp)
                    .fillMaxWidth(),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFFFFFFF)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Recommended location", color = Color(0xFF8288A0),
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            VpnServiceList(activity)
        }
        CustomAlertDialog(activity)
    }
}

@Composable
fun LoadingDialog(activity: ServiceListActivity) {
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

@Composable
fun VpnServiceList(activity: ServiceListActivity) {
    val vpnServiceData = activity.getVpnServiceData()

    if (vpnServiceData[0].ip.isEmpty()) {
        Text(
            text = "No data yet",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(16.dp),
            color = Color.Gray,
            fontSize = 20.sp
        )
    } else {
        LazyColumn {
            items(vpnServiceData.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { activity.selectTheServer(vpnServiceData[index]) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = GetServiceData.getCountryFlag(vpnServiceData[index].country_name)),
                        contentDescription = "Country Flag",
                        modifier = Modifier.requiredSize(30.dp)
                    )
                    Text(
                        text = vpnServiceData[index].country_name,
                        color = Color(0xFF8288A0),
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = activity.showWhetherCheckImg(index)),
                        contentDescription = "Check Image",
                        modifier = Modifier
                            .padding()
                            .requiredSize(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialog(activity: ServiceListActivity) {
    if (activity.showDialog) {
        AlertDialog(
            onDismissRequest = {
                activity.showDialog = false
            },
            title = {
                Text(text = "Tips")
            },
            text = {
                Text("Do you want to confirm that the current server is disconnected?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        activity.showDialog = false
                        activity.backToMain()
                        CanDataUtils.postPointData("antur19")
                    }
                ) {
                    Text("yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        activity.showDialog = false
                    }
                ) {
                    Text("no")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuantumVpnTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF1B6A56)
        ) {
            LoadingDialog(activity = ServiceListActivity())
        }
    }
}

