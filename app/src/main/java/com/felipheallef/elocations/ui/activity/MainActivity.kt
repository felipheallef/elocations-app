package com.felipheallef.elocations.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felipheallef.elocations.R
import com.felipheallef.elocations.data.model.Business
import com.felipheallef.elocations.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val engeselt = Business(1, "Engeselt", "A ENGESELT oferece serviços com foco no alto padrão de qualidade para garantir aos seus clientes uma parceria de sucesso.", "(83) 3268-5743", null, LatLng(-7.1697691, -34.8632688))

        // Add a marker in Sydney and move the camera
        val marker = MarkerOptions()
            .position(engeselt.location)
            .title(engeselt.name)
            .snippet(engeselt.description)

        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(engeselt.location, 15F))

        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        Snackbar.make(
            binding.root,
            "Info window clicked",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}