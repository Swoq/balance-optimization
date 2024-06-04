package com.swoqe.balance_bulk.service

import com.swoqe.balance_bulk.data.BalanceDto
import com.swoqe.balance_bulk.data.User
import com.swoqe.balance_bulk.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    @Value("\${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private var batchSize = 10

    @Transactional
    fun saveUsersInBatches(balances: List<BalanceDto>) {
        balances.forEach { balanceDto ->
            userRepository.updateUserBalance(balanceDto.userId, balanceDto.balance)
        }
//        balances.chunked(batchSize).forEach { batch ->
//            batch.forEach { balanceDto ->
//                userRepository.updateUserBalance(balanceDto.userId, balanceDto.balance)
//            }
//        }
    }

    fun getUsers(page: Int, size: Int): List<User> {
        return userRepository.findAllPaged(size, page * size)
    }
}
