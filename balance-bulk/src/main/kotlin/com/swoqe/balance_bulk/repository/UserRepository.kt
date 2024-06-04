package com.swoqe.balance_bulk.repository

import com.swoqe.balance_bulk.data.User
import java.math.BigDecimal
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE users SET balance = :balance WHERE id = :id", nativeQuery = true)
    fun updateUserBalance(id: Long, balance: BigDecimal): Int

    @Query("SELECT * FROM users LIMIT :limit OFFSET :offset", nativeQuery = true)
    fun findAllPaged(limit: Int, offset: Int): List<User>
}
