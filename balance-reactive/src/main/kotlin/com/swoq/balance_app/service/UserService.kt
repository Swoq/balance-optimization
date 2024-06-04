package com.swoq.balance_app.service

import com.swoq.balance_app.data.User
import com.swoq.balance_app.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(private val userRepository: UserRepository) {

    fun updateUserBalances(balances: Map<Int, Int>): Mono<String> {
        return Flux.fromIterable(balances.entries)
            .flatMap { (id, balance) ->
                userRepository.updateUserBalance(id, balance)
            }.then(Mono.just("Balances have been updated."))
            .onErrorReturn("An error occurred.")
    }

    fun getUsers(page: Int, size: Int): Flux<User> {
        return userRepository.findAllPaged(size, page * size)
    }
}
