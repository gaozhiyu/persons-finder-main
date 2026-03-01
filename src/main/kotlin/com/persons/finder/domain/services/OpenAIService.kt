package com.persons.finder.domain.services

import com.persons.finder.data.Person

interface OpenAIService {
        fun generateSelfIntro(person: Person): Pair<String, String>
        fun getCountryAndCityByCoordinates(latitude: Double, longitude: Double): Pair<String, String>
}