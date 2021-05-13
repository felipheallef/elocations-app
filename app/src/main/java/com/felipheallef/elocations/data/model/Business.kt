package com.felipheallef.elocations.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class Business (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "number")
    val number: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val location: LatLng
    get() = LatLng(this.latitude, this.longitude)
}