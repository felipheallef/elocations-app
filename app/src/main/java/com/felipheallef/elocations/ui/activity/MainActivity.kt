package com.felipheallef.elocations.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.ActivityMainBinding
import com.felipheallef.elocations.ui.fragment.BusinessBottomSheetFragment
import com.felipheallef.elocations.ui.model.BusinessesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File

class MainActivity : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private var guidesActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

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
        if (resultCode == RESULT_OK) {
            setupMapMarkers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        BusinessBottomSheetFragment.getInstance(marker.tag as Business, storageDir).apply {
            this.show(supportFragmentManager, tag)
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

                it.forEach { business ->
                    val markerOptions = MarkerOptions()
                        .position(business.location)
                        .title(business.name)
                        .snippet(business.description)
                    val marker = mMap.addMarker(markerOptions)
                    marker.tag = business
                }

                // Move the camera to the first entry in the business list
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it[0].location, 15F))
            }
        })
    }
}
