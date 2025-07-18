package com.androbeat.androbeatagent.data.model.models.elastic

import com.google.gson.annotations.SerializedName

class ShardsData(
    @field:SerializedName("total") var total: Int, @field:SerializedName(
        "successful"
    ) var successful: Int, @field:SerializedName("failed") var failed: Int
)