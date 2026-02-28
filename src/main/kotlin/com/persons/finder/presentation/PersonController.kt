package com.persons.finder.presentation

import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.PersonsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/persons")
class PersonController(
    private val personsService: PersonsService,
    private val locationsService: LocationsService
) {
    /*
        (JSON) Body and return HTTP 201 when created
    */
    @PostMapping("")
    fun createUser(@RequestBody request: Person): ResponseEntity<String> {
        personsService.save(request)
        return ResponseEntity.status(HttpStatus.CREATED).body("User created")
    }

    /*
        Update/create someone's location using latitude and longitude
        (JSON) Body
     */
    @PutMapping("/{id}/location")
    fun updateLocation(
        @PathVariable id: Long,
        @RequestBody request: Location
    ): ResponseEntity<String> {

        return ResponseEntity.status(HttpStatus.CREATED).body("Location Updated")
    }

        // TODO: hook into LocationsService to persist location for user id
        // Log values so parameters are actually referenced (avoids unused warnings)
    /*
        GET API to retrieve a person by id
     */
    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        val person = personsService.getById(id)
        return ResponseEntity.ok(person)
    }

    @GetMapping("")
    fun getExample(): String {
        return "Hello Example"
    }

}