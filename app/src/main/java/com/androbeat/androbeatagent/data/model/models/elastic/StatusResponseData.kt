package com.androbeat.androbeatagent.data.model.models.elastic

import com.google.gson.annotations.SerializedName

class StatusResponseData(
    @field:SerializedName("name") var name: String,
    @field:SerializedName(
        "cluster_name"
    ) var clusterName: String,
    @field:SerializedName("cluster_uuid") var clusterUuid: String,
    @field:SerializedName(
        "version"
    ) var version: StatusResponseVersionData,
    @field:SerializedName("tagline") var tagline: String
)