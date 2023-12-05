package com.fedoseevdev.newsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedoseevdev.newsapp.MainApplication
import com.fedoseevdev.newsapp.domain.model.News
import com.fedoseevdev.newsapp.domain.model.TotalNews
import com.fedoseevdev.newsapp.utils.Util
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var repository = MainApplication.mainRepository
    private val newsList: MutableList<News> = mutableListOf()
    private var countryCode = ""
    private var apiKey = Util.API_KEY

    val newsLiveData: MutableLiveData<List<News>?> = MutableLiveData()

    private fun getNews(langCode: String, category: String) {
        viewModelScope.launch {
            if (category.isNotEmpty()) {
                repository.getTotalNews(langCode, category, apiKey)?.let {
                    fillNewsList(it)
                }
            } else {
                repository.getTotalNews(langCode, apiKey)?.let {
                    fillNewsList(it)
                }
            }

        }
    }

    private fun getSearchedNews(keyword: String) {
        viewModelScope.launch {
            repository.getSearchedTotalNews(keyword, apiKey)?.let {
                fillNewsList(it)
            }
        }
    }

    private fun fillNewsList(totalNews: TotalNews?) {
        totalNews?.let {
            newsList.clear()
            newsList.addAll(it.newsList)
            newsLiveData.postValue(newsList)
        }
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
        getNews(countryCode, "")
    }

    fun newsCategoryClick(category: String) {
        newsLiveData.value = null
        getNews(countryCode, category)
    }

    fun searchNews(keyword: String) {
        getSearchedNews(keyword)
    }

}