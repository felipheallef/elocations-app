package com.felipheallef.elocations.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felipheallef.elocations.R
import com.felipheallef.elocations.databinding.ActivityMainBinding
import com.felipheallef.elocations.ui.model.BusinessesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap

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
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
//        setupMapMarkers()
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
//        val engeselt = Business("Engeselt", "A ENGESELT oferece serviços com foco no alto padrão de qualidade para garantir aos seus clientes uma parceria de sucesso.", "(83) 3268-5743", -7.1697691, -34.8632688)
//        Application.database?.businessDao()?.insertAll(engeselt)
        setupMapMarkers()

        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        Snackbar.make(
            binding.root,
            "Info window clicked",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setupMapMarkers() {
        val model = BusinessesViewModel(applicationContext)

        model.business.observe(this@MainActivity, {
            // Add a marker to each business in the database

            it.forEach { business ->
                val marker = MarkerOptions()
                    .position(business.location)
                    .title(business.name)
                    .snippet(business.description)
                mMap.addMarker(marker)
            }
            // Move the camera to the first entry in the business list

            if(it.isNotEmpty())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it[0].location, 15F))
        })
    }
}