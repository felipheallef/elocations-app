package com.felipheallef.elocations.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.felipheallef.elocations.Application
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.entity.Business
import com.felipheallef.elocations.databinding.ActivityMainBinding
import com.felipheallef.elocations.ui.fragment.BusinessBottomSheetFragment
import com.felipheallef.elocations.ui.model.BusinessesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var searchView: SearchView
    private lateinit var searchViewAutoComplete: SearchView.SearchAutoComplete
    private var guidesActive = false
    private var businessList = listOf<Business>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ELocations)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.fabAddNew.hide()

        binding.fabAddNew.setOnClickListener {

            val coordinates = mMap.cameraPosition.target
            val extras = Bundle()

            extras.putDouble("latitude", coordinates.latitude)
            extras.putDouble("longitude", coordinates.longitude)

            val intent = Intent(applicationContext, CreateNewActivity::class.java).apply {
                putExtras(extras)
            }
            startActivityForResult(intent, 100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setupMapMarkers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.action_search)
            .actionView as SearchView

        searchViewAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchViewAutoComplete.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                Log.d(TAG, query)
                searchByName(query, true)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                Log.d(TAG, query)
                searchByName(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_toggle_guides -> {
                guidesActive = !guidesActive
                if(guidesActive){
                    item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_visibility_off_24)
                    binding.guides.visibility = View.VISIBLE
                }else{
                    item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_visibility_24)
                    binding.guides.visibility = View.GONE
                }
                true
            }
            R.id.action_settings -> {
//                Intent(applicationContext, SettingsActivity::class.java).apply {
//                    startActivity(this)
//                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        binding.fabAddNew.show()

        setupMapMarkers()

        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val business = marker.tag as Business
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        BusinessBottomSheetFragment.getInstance(business, storageDir).apply {
            this.show(supportFragmentManager, tag)
            this.doAfterDeleted = {
                marker.remove()
            }
            this.doAfterResult = {
//                recreate()
                val newBusiness = Application.database?.businessDao()?.findById(business.id)

                if(newBusiness != null) {
                    marker.title = newBusiness.name
                    marker.snippet = newBusiness.description
                    marker.tag = newBusiness
                    marker.hideInfoWindow()
                }

            }
        }

    }

    /**
     * Add markers on the map for each business stored
     * in the database.
     */
    private fun setupMapMarkers() {
        val model = BusinessesViewModel(applicationContext)

        model.business.observe(this@MainActivity, {
            // Add a marker to each business in the database
            if(it.isNotEmpty()) {
                businessList = it
                it.forEach { business ->
                    val markerOptions = MarkerOptions()
                        .position(business.location)
                        .title(business.name)
                        .snippet(business.description)
                    val marker = mMap.addMarker(markerOptions)
                    marker?.tag = business
                }

                // Move the camera to the first entry in the business list
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it[0].location, 15F))
            }
        })
    }

    fun searchByName(name: String, sendPressed: Boolean = false) {

        val names: ArrayList<String> = arrayListOf()

        val result = businessList.filter {
            it.name.contains(name, ignoreCase = true)
        }

        result.forEach {
             names.add(it.name)
        }

        val resultsAdapter = ArrayAdapter(applicationContext, R.layout.list_item, names)
        searchViewAutoComplete.setAdapter(resultsAdapter)

        searchViewAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val business = businessList.first { it.name == names[position] }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(business.location, 15F))
            Log.d(TAG, "Clicked: ${business.name}")
        }

        if (sendPressed) {
            if (result.isNotEmpty()){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(result[0].location, 15F))
            } else {
                Toast.makeText(applicationContext, "Não foram encontrados nenhum item correspondente à sua pesquisa.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    companion object {
        const val TAG = "MainActivity"
    }

}
