package com.fedoseevdev.newsapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fedoseevdev.newsapp.R
import com.fedoseevdev.newsapp.utils.glidessl.GlideApp

import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {

    companion object {
        @BindingAdapter("bind:imgUrl")
        @JvmStatic
        fun setImage(imageView: ImageView, imgUrl: String?) {
            GlideApp.with(imageView.context).load(imgUrl).placeholder(R.drawable.img_no_image_placeholder)
                .into(imageView)
        }

        @BindingAdapter("app:textToDate")
        @JvmStatic
        fun setTextFromData(textView: TextView, date: Date?) {
            date?.let {
                textView.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    .format(it)
            }

        }
    }
}