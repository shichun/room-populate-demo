package com.motorro.roompopulate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.motorro.roompopulate.cities.data.Coord
import com.motorro.roompopulate.databinding.ActivityMainBinding
import com.motorro.roompopulate.view.CityListAdapter
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainActivityModel by viewModels()
    private val listAdapter = CityListAdapter(this::onCityClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearch()
        setupList()

        model.cities.observe(this, Observer { listAdapter.cities = it })
    }

    /**
     * Opens preferred map application to display a city :)
     */
    private fun onCityClicked(coord: Coord) {
        val uri: String = String.format(Locale.ENGLISH, "geo:%f,%f", coord.lat, coord.lon)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun setupSearch() {
        binding.search.doAfterTextChanged {
            model.search(it?.toString() ?: "")
        }
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        val list = binding.cities
        list.layoutManager = layoutManager
        list.adapter = listAdapter
        list.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(
            list.context,
            layoutManager.orientation
        )
        list.addItemDecoration(itemDecoration)
    }
}
