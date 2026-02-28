package com.persons.finder.repository

import com.persons.finder.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByName(name: String): List<UserEntity>
}