package com.persons.finder.entity

import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

//    @OneToOne(mappedBy = "users", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    val location: LocationEntity? = null
)