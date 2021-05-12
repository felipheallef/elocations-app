package com.felipheallef.elocations.data.model

import com.google.android.gms.maps.model.LatLng

data class Business (
    val id: Int,
    val name: String,
    val description: String,
    val number: String,
    val category: BusinessCategory?,
    val location: LatLng,
        )