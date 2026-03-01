package com.persons.finder.data

data class Location(
    // Tip: Person's id can be used for this field
    val referenceId: Long? = null,
    val latitude: Double,
    val longitude: Double,
    val city: String? = null,
    val country: String? = null,
)
