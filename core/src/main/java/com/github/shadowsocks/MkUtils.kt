package com.github.shadowsocks


import android.net.VpnService

object MkUtils {


    fun brand(builder: VpnService.Builder, myPackageName: String) {
        (listOf(myPackageName) + listGmsPackages())
            .iterator()
            .forEachRemaining {
                runCatching { builder.addDisallowedApplication(it) }
            }
    }

    private fun listGmsPackages(): List<String> {
        return listOf(
            "com.google.android.gms",
            "com.google.android.ext.services",
            "com.google.process.gservices",
            "com.android.vending",
            "com.google.android.gms.persistent",
            "com.google.android.cellbroadcastservice",
            "com.google.android.packageinstaller",
            "com.google.android.gms.location.history",
        )
    }
}