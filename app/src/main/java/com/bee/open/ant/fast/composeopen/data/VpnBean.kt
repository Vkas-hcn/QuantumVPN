package com.bee.open.ant.fast.composeopen.data

import com.google.gson.annotations.SerializedName

data class VpnBean(
    val code: Int,
    val `data`: Data,
    val msg: String
)

data class Data(
    @SerializedName("UNjLql")
    val server_list: List<ServerVpn> = emptyList(),
    @SerializedName("sbpEFuLOyA")
    val smart_list: List<ServerVpn> = emptyList()
)

data class ServerVpn(
    @SerializedName("kkGOFJjYT")
    val city: String,

    @SerializedName("bxZYF")
    val country_code: String,

    @SerializedName("AbFoGvxxq")
    var country_name: String,

    @SerializedName("DjjMmBkjVp")
    val ip: String,

    @SerializedName("HjdDPpbScp")
    val mode: String,

    @SerializedName("HtCu")
    val port: Int,

    @SerializedName("QOqNS")
    val user_name: String,

    @SerializedName("quYT")
    val user_pwd: String,
    var isBest: Boolean = false
)