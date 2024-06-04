package com.swoqe.balance_bulk.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import java.math.BigDecimal

@Entity(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    val id: Long?,
    val name: String,
    var balance: BigDecimal
) {

    constructor(name: String, balance: BigDecimal) : this(null, name, balance)
}
