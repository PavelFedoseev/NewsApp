package com.fedoseevdev.newsapp.ui.activities

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fedoseevdev.newsapp.R
import com.fedoseevdev.newsapp.ui.adapters.AdapterListNews
import com.fedoseevdev.newsapp.ui.clicklisteners.AdapterItemClickListener
import com.fedoseevdev.newsapp.ui.clicklisteners.NewsDialogClickListener
import com.fedoseevdev.newsapp.databinding.NewsDialogBinding
import com.fedoseevdev.newsapp.utils.extensions.changeMenuIconColor
import com.fedoseevdev.newsapp.utils.extensions.setSystemBarColor
import com.fedoseevdev.newsapp.domain.model.News
import com.fedoseevdev.newsapp.utils.LocaleHelper
import com.fedoseevdev.newsapp.utils.Util
import com.fedoseevdev.newsapp.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), AdapterItemClickListener {

    companion object {
        const val firstTimeExecution = "firstControl"
        const val countryPositionPref = "countryPositionPref"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivToolbarCountry: ImageView
    private lateinit var tvAppLabel: TextView
    private lateinit var tvSelectedCategory: TextView

    private lateinit var cvBusiness: CardView
    private lateinit var cvEntertainment: CardView
    private lateinit var cvHealth: CardView
    private lateinit var cvScience: CardView
    private lateinit var cvSport: CardView
    private lateinit var cvTechnology: CardView

    private lateinit var viewModel: MainViewModel
    private lateinit var countries: Array<String>
    private lateinit var countriesCodes: Array<String>
    private lateinit var countriesIcons: TypedArray
    private lateinit var pref: SharedPreferences

    private lateinit var adapterListNews: AdapterListNews
    private var newsList: MutableList<News> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initVariables()
        initViews()
        initToolbar()
        initObservers()
        initRecyclerViewAdapters()
        languageControl()
    }

    private fun initViews() {
        ivToolbarCountry = findViewById(R.id.ivToolbarCountry)
        ivToolbarCountry.setOnClickListener {
            showLanguageDialog()
        }
        recyclerView = findViewById(R.id.recyclerView)
        tvAppLabel = findViewById(R.id.tv_app_label)
        tvSelectedCategory = findViewById(R.id.tv_selected_category_name)

        cvBusiness = findViewById(R.id.cardBusiness)
        cvEntertainment = findViewById(R.id.cardEntertainment)
        cvHealth = findViewById(R.id.cardHealth)
        cvScience = findViewById(R.id.cardScience)
        cvSport = findViewById(R.id.cardSports)
        cvTechnology = findViewById(R.id.cardTechnology)

        tvAppLabel.setOnClickListener {
            viewModel.newsCategoryClick("")
            tvSelectedCategory.text = getString(R.string.news)
        }
        cvBusiness.setOnClickListener {
            viewModel.newsCategoryClick("business")
            tvSelectedCategory.text = getString(R.string.category_business)
        }
        cvEntertainment.setOnClickListener {
            viewModel.newsCategoryClick("entertainment")
            tvSelectedCategory.text = getString(R.string.category_entertainment)
        }
        cvHealth.setOnClickListener {
            viewModel.newsCategoryClick("health")
            tvSelectedCategory.text = getString(R.string.category_health)
        }
        cvScience.setOnClickListener {
            viewModel.newsCategoryClick("science")
            tvSelectedCategory.text = getString(R.string.category_science)
        }
        cvSport.setOnClickListener {
            viewModel.newsCategoryClick("sports")
            tvSelectedCategory.text = getString(R.string.category_sports)
        }
        cvTechnology.setOnClickListener {
            viewModel.newsCategoryClick("technology")
            tvSelectedCategory.text = getString(R.string.category_technology)
        }

    }

    private fun initVariables() {
        countries = resources.getStringArray(R.array.countries)
        countriesIcons = resources.obtainTypedArray(R.array.countrysIcons)
        countriesCodes = resources.getStringArray(R.array.countrysCodes)

        pref = applicationContext.getSharedPreferences(Util.APP_NAME, MODE_PRIVATE)
    }

    private fun initRecyclerViewAdapters() {
        adapterListNews = AdapterListNews(newsList, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = null
        recyclerView.adapter = adapterListNews
    }

    private fun initObservers() {
        viewModel.newsLiveData.observe(this) { news ->
            newsList.clear()
            news?.let {
                newsList.addAll(it)
                adapterListNews.notifyItemRangeChanged(0, it.size)
            }
        }
    }

    private fun languageControl() {
        if (!pref.getBoolean(firstTimeExecution, false)) {
            LocaleHelper.setLocale(applicationContext, countriesCodes[0])
            pref.edit().putBoolean(firstTimeExecution, true).apply()
            recreate()
        }
        setupCountryCode()
        setupLangImageIcon()
    }

    private fun setupCountryCode() {
        pref.getString(Util.COUNTRY_PREF, countriesCodes[0])
            ?.let { viewModel.setCountryCode(it) }
    }

    private fun setupLangImageIcon() {
        val langIconId = if (pref.contains(countryPositionPref))
            countriesIcons.getResourceId(
                pref.getInt(
                    countryPositionPref,
                    0
                ), 0
            )
        else
            countriesIcons.getResourceId(0, 0)
        ivToolbarCountry.setImageResource(langIconId)
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setSystemBarColor(R.color.windowBackground)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        changeMenuIconColor(menu, ContextCompat.getColor(this, R.color.windowTextColor))
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.let { it ->
            it.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            it.queryHint = getString(R.string.search_in_everything)
            it.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchNews(query)
                    tvSelectedCategory.text = getString(R.string.news)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
        return true
    }

    private fun showLanguageDialog() {
        AlertDialog.Builder(this).setCancelable(false)
            .setTitle(this.getString(R.string.choose_language))
            .setSingleChoiceItems(countries, pref.getInt(countryPositionPref, 0), null)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val selectedPosition =
                    (dialog as AlertDialog).listView
                        .checkedItemPosition
                pref.edit().putInt(countryPositionPref, selectedPosition).apply()
                pref.edit().putString(
                    Util.COUNTRY_PREF,
                    resources.getStringArray(R.array.countrysCodes)[selectedPosition]
                ).apply()
                LocaleHelper.setLocale(
                    applicationContext,
                    resources.getStringArray(R.array.countrysCodes)[selectedPosition]
                )
                recreate()
                dialog.dismiss()
            }
            .show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onNewsItemClick(news: News?) {
        showDialogPolygon(news)
    }

    private fun showDialogPolygon(news: News?) {
        val dialog = Dialog(this)
        val binding: NewsDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext), R.layout.dialog_header_polygon,
            null, false
        )
        binding.news = news
        binding.listener = object : NewsDialogClickListener {
            override fun onGotoWebSiteClick(url: String?) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            override fun onDismissClick() {
                dialog.dismiss()
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
    }

}