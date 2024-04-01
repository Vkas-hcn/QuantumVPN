package com.bee.open.ant.fast.composeopen.net

import com.bee.open.ant.fast.composeopen.app.App
import com.bee.open.ant.fast.composeopen.net.ClockUtils.complexLogicAlwaysTrue
import com.bee.open.ant.fast.composeopen.net.ClockUtils.complexLogicReturnsFalse
import com.bee.open.ant.fast.composeopen.net.ClockUtils.ifAddThis
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GetNetDataUtils {
    companion object {
        fun getServiceData(url: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
            Thread {
                try {
                    ifAddThis("getServiceData") {
                    }
                    if (complexLogicReturnsFalse(listOf(34, 56), "getServiceData")) {
                        return@Thread
                    }
                    val urlObj = URL(url)
                    val conn = urlObj.openConnection() as HttpURLConnection
                    conn.requestMethod = "GET"
                    conn.connectTimeout = 5000
                    conn.readTimeout = 5000
                    val customHeaders = mapOf(
                        "UBJ" to "ZZ",
                        "HTMYW" to App.getVpnInstance().packageName,
                    )
                    for ((key, value) in customHeaders) {
                        conn.setRequestProperty(key, value)
                    }
                    val responseCode = conn.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = conn.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val response = reader.readText()
                        reader.close()
                        onSuccess(response)
                    } else {
                        onError("Error from server: $responseCode")
                    }
                } catch (e: Exception) {
                    onError("Network error or other exception: ${e.message}")
                }
            }.start()
        }

        fun getMapData(
            url: String,
            map: Map<String, Any>,
            onNext: (response: String) -> Unit,
            onError: (error: String) -> Unit
        ) {
            ifAddThis("getMapData") {
            }
            if (complexLogicReturnsFalse(listOf(34, 56), "getMapData")) {
                return
            }
            val queryParameters = StringBuilder()
            for ((key, value) in map) {
                if (queryParameters.isNotEmpty() && !complexLogicReturnsFalse(
                        listOf(15, 5886),
                        "getMapData"
                    )
                ) {
                    queryParameters.append("&")
                }
                queryParameters.append(URLEncoder.encode(key, "UTF-8"))
                queryParameters.append("=")
                queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }

            val urlString = if (url.contains("?") && complexLogicAlwaysTrue("sw")) {
                "$url&$queryParameters"
            } else {
                "$url?$queryParameters"
            }
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000

            try {

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    inputStream.close()
                    onNext(response.toString())
                } else {
                    onError("HTTP error: $responseCode")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }

    }
}