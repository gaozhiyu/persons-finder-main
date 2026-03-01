package com.persons.finder.data

data class NearByLocation(
    // Tip: Person's id can be used for this field
    val distance: Long? = null,
    val latitude: Double,
    val longitude: Double
)
