package com.persons.finder.entity

import com.persons.finder.entity.UserEntity
import javax.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "locations")
data class LocationEntity(

    @Id
    @Column(name = "reference_id")
    val referenceId: Long = 0,

    @Column(nullable = false, precision = 10, scale = 7)
    val latitude: BigDecimal,

    @Column(nullable = false, precision = 10, scale = 7)
    val longitude: BigDecimal,

    @Column(nullable = false)
    val country: String,

    @Column(nullable = false)
    val city: String,

//    @MapsId
//    @OneToOne
//    @JoinColumn(name = "reference_id")
//    val user: UserEntity
){
    protected constructor() : this(0, BigDecimal.ZERO, BigDecimal.ZERO, "", "")
}