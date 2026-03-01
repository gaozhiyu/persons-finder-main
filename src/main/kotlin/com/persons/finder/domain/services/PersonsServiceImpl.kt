package com.persons.finder.domain.services

import com.persons.finder.data.Person
import com.persons.finder.entity.UserEntity
import com.persons.finder.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl(
    private val userRepository: UserRepository
) : PersonsService {

    override fun getById(id: Long): Person {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found: $id") }

        // Map UserEntity to Person. The UserEntity currently only contains id and name,
        // so fill missing fields with reasonable defaults.
        return Person(
            id = user.id,
            name = user.name,
            hobby = "",
            jobTitle = ""
        )
    }

    override fun save(person: Person) : Long{
        val entity = UserEntity(
            id = person.id,
            name = person.name
        )
        val p = userRepository.save(entity)
        return p.id;
    }

}