package com.androbeat.androbeatagent.data.model.models.elastic

import com.google.gson.annotations.SerializedName

class ResponseData(
    @field:SerializedName("_shards") var shards: ShardsData, @field:SerializedName(
        "_index"
    ) var index: String, @field:SerializedName("_type") var type: String, @field:SerializedName(
        "_id"
    ) var id: String, @field:SerializedName("_version") var version: Int, @field:SerializedName(
        "result"
    ) var result: String, @field:SerializedName("_seq_no") var seqNo: Int, @field:SerializedName(
        "_primary_term"
    ) var primaryTerm: Int
)