package com.persons.finder.domain.services

import com.persons.finder.data.Location
import com.persons.finder.entity.LocationEntity
import com.persons.finder.entity.UserEntity
import com.persons.finder.repository.LocationRepository
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate

@Service
class LocationsServiceImpl (
    private val locationsRepository: LocationRepository,
    private val openAIService: OpenAIService,
    private val personsService: PersonsService,
    private val db: JdbcTemplate
): LocationsService {

    override fun addLocation(location: Location) : Location {

        val id = location.referenceId;
        if (id === null){
            throw NoSuchElementException("Person not found: ${location.referenceId}")
        }
        val p = personsService.getById(id) // Check if person exists, will throw if not
        if (p == null) {
            throw NoSuchElementException("Person not found: ${location.referenceId}")
        }
        val (country, city) = openAIService.getCountryAndCityByCoordinates(location.latitude, location.longitude)
        println("OpenAI returned country: $country, city: $city for coordinates: ${location.latitude}, ${location.longitude}")
        val locationToSave = LocationEntity(
            referenceId = p.id,
            latitude = location.latitude.toBigDecimal(),
            longitude = location.longitude.toBigDecimal(),
            country = country,
            city = city
        )
        locationsRepository.save(locationToSave);
        return location.copy(country = country, city = city)
    }

    override fun removeLocation(locationReferenceId: Long) {
        TODO("Not yet implemented")
    }

    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        // Use OpenAI to infer country/city of the query point to reuse country/city filtering
        val (country, city) = openAIService.getCountryAndCityByCoordinates(latitude, longitude)
        println("OpenAI returned country: $country, city: $city for coordinates: ${latitude}, ${longitude}")

        // Call repository native query which computes distance using Haversine formula and filters by country/city
        val entities = locationsRepository.findWithinRadiusByCountryCity(latitude, longitude, radiusInKm, country, city)

        // Map entities to domain model and return
        return entities.map { e ->
            Location(
                referenceId = e.referenceId,
                latitude = e.latitude.toDouble(),
                longitude = e.longitude.toDouble(),
                city = e.city,
                country = e.country
            )
        }
    }

}