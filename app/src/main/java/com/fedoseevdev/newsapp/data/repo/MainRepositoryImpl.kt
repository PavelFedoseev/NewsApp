package com.fedoseevdev.newsapp.data.repo

import android.util.Log
import com.fedoseevdev.newsapp.data.restapi.NewsApi
import com.fedoseevdev.newsapp.domain.model.TotalNews
import com.fedoseevdev.newsapp.domain.repositories.MainRepository

private const val TAG = "MainRepositoryImpl"

class MainRepositoryImpl(private val service: NewsApi) : MainRepository {


    override suspend fun getTotalNews(country: String, apiKey: String): TotalNews? {
        return try {
            val response = service.getTotalNews(country, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTotalNews error", e)
            null
        }
    }

    override suspend fun getTotalNews(
        country: String,
        category: String,
        apiKey: String,
    ): TotalNews? {
        return try {
            val response = service.getTotalNews(country, category, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTotalNews error", e)
            null
        }

    }

    override suspend fun getSearchedTotalNews(
        keyword: String,
        apiKey: String,
    ): TotalNews? {
        return try {
            val response = service.getSearchedTotalNews(keyword, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getSearchedTotalNews error", e)
            null
        }
    }
}
