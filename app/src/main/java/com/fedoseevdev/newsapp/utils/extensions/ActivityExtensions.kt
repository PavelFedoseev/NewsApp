package com.fedoseevdev.newsapp.utils.extensions

import android.app.Activity
import android.graphics.PorterDuff
import android.view.Menu
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat


fun Activity.setSystemBarColor(@ColorRes color: Int) {
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ResourcesCompat.getColor(this.resources, color, null)
}

fun changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
    for (i in 0 until menu.size()) {
        val drawable = menu.getItem(i).icon ?: continue
        drawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

