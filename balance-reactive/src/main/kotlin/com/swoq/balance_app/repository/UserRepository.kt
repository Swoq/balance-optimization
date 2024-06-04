package com.swoq.balance_app.repository

import com.swoq.balance_app.data.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveCrudRepository<User, Int> {

    @Query("UPDATE users SET balance = :balance WHERE id = :id")
    fun updateUserBalance(id: Int, balance: Int): Mono<Void>

    @Query("SELECT * FROM users LIMIT :limit OFFSET :offset")
    fun findAllPaged(limit: Int, offset: Int): Flux<User>
}
