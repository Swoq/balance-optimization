package com.swoq.balance_app.data

import java.math.BigDecimal
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    @Id val id: Int?,
    val name: String,
    var balance: BigDecimal
) {
    constructor(name: String, balance: BigDecimal) : this(null, name, balance)
}
