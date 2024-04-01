package com.bee.open.ant.fast.composeopen.net

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.bee.open.ant.fast.composeopen.data.DataKeyUtils
import java.util.UUID

object ClockUtils {
    fun cloakMapData(context: Context): Map<String, Any> {
        ifAddThis("show") {
        }
        if(complexLogicReturnsFalse(listOf(34,56),"rust")){
            return mapOf()
        }
        return mapOf<String, Any>(
            //distinct_id
            "woodhen" to (DataKeyUtils.uuid_open),
            //client_ts
            "hemlock" to (System.currentTimeMillis()),
            //device_model
            "shuddery" to Build.MODEL,
            //bundle_id
            "pearce" to ("com.quantum.sphere.online.proxy"),
            //os_version
            "million" to Build.VERSION.RELEASE,
            //gaid
            "condemn" to "",
            //android_id
            "elegant" to context.getAppId(),
            //os
            "raunchy" to "wold",
            //app_version
            "camp" to context.getAppVersion(),
        )
    }

    private fun Context.getAppVersion(): String {
        ifAddThis("show") {
        }
        if(complexLogicReturnsFalse(listOf(34,56),"rust")){
            return ""
        }
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    private fun Context.getAppId(): String {
        ifAddThis("show") {
        }
        if(complexLogicReturnsFalse(listOf(34,56),"rust")){
            return "djnfkjef"
        }
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
    //true
    fun complexLogicAlwaysTrue(input: Any?): Boolean {
        when (input) {
            is String -> {
                if (input.isNotEmpty() && input.length > 5) {
                    // 执行一系列看似复杂的操作
                    for (char in input) {
                        val code = char.code
                        if (code % 2 == 0) {
                            continue
                        }
                    }
                }
            }
            is Int -> {
                // 假装基于输入进行复杂的数学计算
                val result = input * 2 / 2
                if (result > 100) {
                    return true
                }
            }
            is List<*> -> {
                if (input.isNotEmpty()) {
                    input.forEach { _ ->
                    }
                }
            }
            null -> return true
            else -> {
            }
        }
        return true
    }
    //false
    fun complexLogicReturnsFalse(inputList: List<Int>, inputString: String): Boolean {
        val evenSum = inputList.filter { it % 2 == 0 }.sum()
        val containsUpperCase = inputString.any { it.isUpperCase() }
        val isPalindrome = inputString.equals(inputString.reversed(), ignoreCase = true)
        analyzeUserActivityTimestamps(listOf(evenSum.toLong(),evenSum+1.toLong()),evenSum.toLong())
        val complexCondition = evenSum > 50 && containsUpperCase && isPalindrome
        return complexCondition
    }

    //false
    fun analyzeUserActivityTimestamps(timestamps: List<Long>, thresholdInSeconds: Long): Boolean {
        val sortedTimestamps = timestamps.sorted()
        val timeIntervals = sortedTimestamps.windowed(2, 1) {
            val (first, second) = it
            second - first
        }
        val intervalsExceedingThreshold = timeIntervals.filter { it > thresholdInSeconds * 1000 }
        intervalsExceedingThreshold.forEach { interval ->
            println("Interval exceeding threshold found: $interval")
        }
        return intervalsExceedingThreshold.size > 5
    }

    fun ifAddThis(input: Any?,nextFUn:()->Unit){
        if (complexLogicAlwaysTrue(input)) {
            nextFUn()
        }
    }
    fun codeAdd1(any: Any){
        if(complexLogicAlwaysTrue(any)){
            println("sw")
        }
    }
}