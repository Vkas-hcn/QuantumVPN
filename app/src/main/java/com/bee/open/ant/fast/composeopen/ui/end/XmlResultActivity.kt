package com.bee.open.ant.fast.composeopen.ui.end

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bee.open.ant.fast.composeopen.R
import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.databinding.ActivityMainBinding
import com.bee.open.ant.fast.composeopen.databinding.ActivityResultBinding
import com.bee.open.ant.fast.composeopen.load.BaseAdLoad
import com.bee.open.ant.fast.composeopen.load.DishNomadicLoad
import com.bee.open.ant.fast.composeopen.load.NativeAdLoad
import com.bee.open.ant.fast.composeopen.load.NativeAdLoadDis
import com.bee.open.ant.fast.composeopen.net.CanDataUtils
import com.bee.open.ant.fast.composeopen.net.ClockUtils
import com.bee.open.ant.fast.composeopen.net.GetServiceData
import com.bee.open.ant.fast.composeopen.ui.theme.QuantumVpnTheme
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable.isCancelled
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class XmlResultActivity : AppCompatActivity() {
    var jobDialog: Job? = null
    var adJobDialog: Job? = null
    var jobBackDialog: Job? = null
    val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        clickFun()
        showNativeAd()
        showSwitchDialogFun()
        CanDataUtils.antur12()
    }

    private fun initData() {
        if (App.isVpnState == 2) {
            binding.tvState1.text = "Connection succeed"
            binding.tvState2.text = "You are very safe right now!"
            binding.imageView.setImageResource(R.drawable.ic_connect)
        } else {
            binding.tvState1.text = "Disconnection succeed"
            binding.tvState2.text = "You have exposed in danger!"
            binding.imageView.setImageResource(R.drawable.ic_disconnect)
        }
    }

    private fun clickFun() {
        binding.imgX.setOnClickListener {
            cloneDialogFun()
        }
        binding.iconBack.setOnClickListener {
            backFun()
        }
        binding.tvSwitch.setOnClickListener {
            App.showSwitchState = true
            binding.showSwitch = false
            finish()
            CanDataUtils.postPointData(
                "antur27",
                "qu",
                App.top_activity_Quan?.javaClass?.simpleName,
            )
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
            if (binding?.showSwitch == true) {
                return@addCallback
            }
            backFun()
        }
    }

    private fun showSwitchDialogFun() {
        if (App.isVpnState == 2 && (!App.showSwitchState) && (!DishNomadicLoad.getBuyingShieldData()) && DishNomadicLoad.showAdBlacklist() && DishNomadicLoad.getIntervalTimes()) {
            Log.e("TAG", "binding.showSwitch = true")
            binding.showSwitch = true
            BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
            CanDataUtils.postPointData(
                "antur26",
                "qu",
                App.top_activity_Quan?.javaClass?.simpleName,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adJobDialog?.cancel()
//        if (NativeAdLoad.nativeAdEnd != null && App.getVpnState()) {
//            NativeAdLoad.nativeAdEnd?.destroy()
//        }
//        if (NativeAdLoadDis.nativeAdEnd != null&& !App.getVpnState()) {
//            NativeAdLoad.nativeAdEnd?.destroy()
//        }
    }

    private fun showNativeAd() {
        adJobDialog?.cancel()
        adJobDialog = null
        val endNav = BaseAdLoad.getEndNativeAdData()
        endNav.preload(this)
        adJobDialog = lifecycleScope.launch {
            delay(300)
            runCatching {
                while (true) {
                    if (endNav.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
                        endNav.showFullScreenAdBIUYBUI(this@XmlResultActivity) {
                            adJobDialog?.cancel()
                            adJobDialog = null
                        }
                        break
                    }
                    delay(500)
                }
            }
        }
    }


    fun backFun() {
        CanDataUtils.postPointData("antur21")
        showInt2Ad {
            finish()
        }
    }

    private fun showInt2Ad(nextFun: () -> Unit) {
        if (DishNomadicLoad.getBuyingShieldData() || !DishNomadicLoad.showAdBlacklist() || !BaseAdLoad.canShowAD()) {
            nextFun()
            return
        }

        val inter = BaseAdLoad.getInterResultAdData()
        inter.preload(this)
        binding.showIntAd = true
        jobBackDialog?.cancel()
        jobBackDialog = null
        val max = 5 * 10
        jobBackDialog = GetServiceData.countDown(max, 100, MainScope(), { num ->
            if (num > 10 && inter.haveCache && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.showIntAd = false
                inter.showFullScreenAdBIUYBUI(this@XmlResultActivity) {
                    nextFun()
                }
                jobBackDialog?.cancel()
                jobBackDialog = null
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
        jobDialog?.cancel()
        jobDialog = null
        val max = seconds * 10
        jobDialog = GetServiceData.countDown(max, 100, MainScope(), {
            if (it > 10) {
                BaseAdLoad.showDialogAdIfCan(this@XmlResultActivity) {
                    if (this.lifecycle.currentState == Lifecycle.State.RESUMED || this.lifecycle.currentState == Lifecycle.State.STARTED) {
                        nextFun()
                        BaseAdLoad.interHaHaHaOPNNOPIN.preload(this)
                    }
                }
            }
        }, {
            nextFun()
        })
    }

    fun cloneDialogFun() {
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
}
