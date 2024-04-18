package com.bee.open.ant.fast.composeopen.ui.end

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuantumVpnTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1B6A56)
                ) {
                    titleView(this@ResultActivity)
                }
            }
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
            backFun()
        }
    }
    fun backFun(){
        showInt2Ad{
            finish()
        }
    }
    private fun showInt2Ad(nextFun:()->Unit) {
        if (!DishNomadicLoad.showAdBlacklist() || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }
        if (BaseAdLoad.interHaHaHaOPNNOPIN2.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
            BaseAdLoad.interHaHaHaOPNNOPIN2.showFullScreenAdBIUYBUI(this) {
                nextFun()
            }
        }
        else
            nextFun()
    }
}


@Composable
fun titleView(activity: ResultActivity) {
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
        Text(
            text = if (App.isVpnState == 2) "Connection succeed" else "Disconnection succeed",
            color = Color.White,
            fontSize = 20.sp,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text = if (App.isVpnState == 2) "You are very safe right now!" else "You have exposed in danger!",
            color = Color(0xFFADE2C2),
            fontSize = 12.sp,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Image(
            painter = painterResource(id = if (App.isVpnState == 2) R.drawable.ic_connect else R.drawable.ic_disconnect),
            contentDescription = "Connect Success",
            modifier = Modifier
                .padding(top = 20.dp)
                .requiredSize(166.dp)
                .fillMaxWidth(),
            alignment = Alignment.Center
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
            titleView(activity = ResultActivity())
        }
    }
}

