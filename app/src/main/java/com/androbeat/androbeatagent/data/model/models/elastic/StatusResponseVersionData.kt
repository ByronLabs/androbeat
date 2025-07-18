package com.androbeat.androbeatagent.data.model.models.elastic

import com.google.gson.annotations.SerializedName

class StatusResponseVersionData(
    @field:SerializedName(
        "number"
    ) var number: String,
    @field:SerializedName("build_flavor") var buildFlavor: String,
    @field:SerializedName(
        "build_type"
    ) var buildType: String,
    @field:SerializedName("build_hash") var buildHash: String,
    @field:SerializedName(
        "build_date"
    ) var buildDate: String,
    @field:SerializedName("build_snapshot") var buildSnapshot: Boolean,
    @field:SerializedName(
        "lucene_version"
    ) var luceneVersion: String,
    @field:SerializedName("minimum_wire_compatibility_version") var minimumWireCompatibilityVersion: String,
    @field:SerializedName(
        "minimum_index_compatibility_version"
    ) var minimumIndexCompatibilityVersion: String
)