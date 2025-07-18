package com.androbeat.androbeatagent.data.model.models.communication


data class Device (

    var token: String = "",

    var deviceId: String = "",

    var model: String = "",

    var manufacturer: String = "",

    var mainAccountName: String = "",

    var clientId: String = "",

    var status: Int = 1,

)