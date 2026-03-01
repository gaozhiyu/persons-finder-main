package com.persons.finder.repository

import com.persons.finder.entity.LocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface LocationRepository : JpaRepository<LocationEntity, Long> {
    fun findByCountryAndCity(country: String, city: String): List<LocationEntity>

    // Native query using Haversine formula (distance in kilometers)
    // Using positional parameters (?) instead of named parameters for PostgreSQL compatibility
    @Query(value = """
        select
            reference_id,
            latitude,
            longitude,
            country,
            city
        from
            (
            select
                reference_id,
                latitude,
                longitude,
                country,
                city,
                		6371 * acos(
           greatest(-1, least(1,
               cos(radians(?1)) *
               cos(radians(latitude)) *
               cos(radians(longitude) - radians(?2)) +
               sin(radians(?1)) *
               sin(radians(latitude))
           ))
       ) AS dis
            from
                locations )
        where
            dis < ?3
            and country = ?4
            and city = ?5 order by dis asc
    """, nativeQuery = true)
    fun findWithinRadiusByCountryCity(
        lat: Double,
        lon: Double,
       radius: Double,
       country: String,
       city: String
    ): List<LocationEntity>

//    fun findByUserid(userid: Long): Optional<LocationEntity>
}