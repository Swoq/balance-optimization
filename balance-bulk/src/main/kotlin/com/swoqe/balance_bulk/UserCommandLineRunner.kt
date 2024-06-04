package com.swoqe.balance_bulk

import com.swoqe.balance_bulk.data.User
import com.swoqe.balance_bulk.repository.UserRepository
import java.math.BigDecimal
import java.security.SecureRandom
import java.util.UUID
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("generate-100k")
class UserCommandLineRunner(
    private val userRepository: UserRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val random = SecureRandom()
        val users = (1..100000).map {
            User(
                name = UUID.randomUUID().toString(),
                balance = BigDecimal(random.nextDouble(1.0, 100000.0).toString())
            )
        }

        userRepository.saveAll(users)

    }
}
