package com.fedoseevdev.newsapp.domain.model

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("name")
    var sourceName: String = String(),
    @SerializedName("id")
    var sourceId: String = String()
)
