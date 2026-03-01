package com.persons.finder.presentation

import com.persons.finder.data.Location
import com.persons.finder.data.NearByLocation
import com.persons.finder.data.Person
import com.persons.finder.data.PersonResponse
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.OpenAIService
import com.persons.finder.domain.services.PersonsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/persons")
class PersonController(
    private val personsService: PersonsService,
    private val locationsService: LocationsService,
    private val openAIService: OpenAIService
) {
    /*
        (JSON) Body and return HTTP 201 when created
    */
    @PostMapping("")
    fun createUser(@RequestBody request: Person): ResponseEntity<PersonResponse> {
        val (intro, summary) = openAIService.generateSelfIntro(request)
        val id  = personsService.save(request)
        val p = PersonResponse(
            id = id,
            bio = intro,
            summary = summary
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(p)
    }

    /*
        Update/create someone's location using latitude and longitude
        (JSON) Body
     */
    @PutMapping("/{id}/location")
    fun updateLocation(
        @PathVariable id: Long,
        @RequestBody request: Location
    ): ResponseEntity<Location> {

        var copy = request.copy(referenceId = id);
        val result = locationsService.addLocation(copy)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    /*
        GET API to retrieve a person by id
     */
    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: Long): ResponseEntity<Person> {
        val person = personsService.getById(id)
        return ResponseEntity.ok(person)
    }

    /*
    GET API to retrieve a person by id
 */
    @GetMapping("/nearby")
    fun getNearBys(@RequestBody request: NearByLocation): ResponseEntity<List<Location>> {
        val result = locationsService.findAround(request.latitude ,request.longitude, request.distance?.toDouble() ?: 5.0)
        return ResponseEntity.ok(result)
    }

    @GetMapping("")
    fun getExample(): String {
        return "Hello Example"
    }

}