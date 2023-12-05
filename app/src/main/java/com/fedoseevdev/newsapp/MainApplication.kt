package com.fedoseevdev.newsapp

import android.app.Application
import android.content.Context
import com.fedoseevdev.newsapp.domain.repositories.MainRepository
import com.fedoseevdev.newsapp.data.repo.MainRepositoryImpl
import com.fedoseevdev.newsapp.data.restapi.NewsApi
import com.fedoseevdev.newsapp.utils.UnsafeOkHttpClient
import com.fedoseevdev.newsapp.utils.Util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: MainApplication
            private set

        @JvmStatic
        lateinit var appContext: Context
            private set

        @JvmStatic
        lateinit var mainRepository: MainRepository
            private set

        @JvmStatic
        lateinit var newsApi: NewsApi
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        initRetrofitApi()
        mainRepository = MainRepositoryImpl(newsApi)
    }

    private fun initRetrofitApi() {
        newsApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Util.API_BASE_URL)
            .client(UnsafeOkHttpClient.unsafeOkHttpClient)
            .build()
            .create(NewsApi::class.java)
    }


}