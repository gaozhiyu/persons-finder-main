package com.persons.finder.repository

import com.persons.finder.entity.LocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationRepository : JpaRepository<LocationEntity, Long> {
    fun findByCountryAndCity(country: String, city: String): List<LocationEntity>
//    fun findByUserid(userid: Long): Optional<LocationEntity>
}