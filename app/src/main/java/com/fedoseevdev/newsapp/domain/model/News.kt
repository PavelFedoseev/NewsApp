package com.fedoseevdev.newsapp.domain.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class News(
    @SerializedName("source")
    var source: Source = Source(),

    @SerializedName("title")
    val newsTitle: String = String(),

    @SerializedName("description")
    val newsDescription: String = String(),

    @SerializedName("url")
    val newsUrl: String = String(),

    @SerializedName("urlToImage")
    val newsImage: String = String(),

    @SerializedName("publishedAt")
    var newsPublishedDate: Date = Date(),
)