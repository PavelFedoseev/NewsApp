package com.fedoseevdev.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fedoseevdev.newsapp.R
import com.fedoseevdev.newsapp.ui.clicklisteners.AdapterItemClickListener
import com.fedoseevdev.newsapp.databinding.NewsBinding
import com.fedoseevdev.newsapp.domain.model.News

class AdapterListNews(
    private val items: List<News> = emptyList(),
    private val adapterItemClickListener: AdapterItemClickListener,
) : RecyclerView.Adapter<AdapterListNews.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsBinding: NewsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_news_dashboard,
            parent,
            false
        )
        return NewsViewHolder(newsBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), adapterItemClickListener)
    }

    override fun getItemCount() = items.size

    private fun getItem(position: Int) = items[position]

    inner class NewsViewHolder(private var newsBinding: NewsBinding) :
        RecyclerView.ViewHolder(newsBinding.root) {

        fun bind(news: News?, adapterItemClickListener: AdapterItemClickListener?) {
            newsBinding.news = news
            newsBinding.clickListener = adapterItemClickListener

        }
    }
}



