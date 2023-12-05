package com.fedoseevdev.newsapp.domain.repositories

import com.fedoseevdev.newsapp.domain.model.TotalNews

interface MainRepository {

    suspend fun getTotalNews(country: String, apiKey: String): TotalNews?

    suspend fun getTotalNews(country: String, category: String, apiKey: String): TotalNews?

    suspend fun getSearchedTotalNews(keyword: String, apiKey: String): TotalNews?
}

