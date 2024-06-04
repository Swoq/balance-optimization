package com.swoq.balance_app

import com.swoq.balance_app.data.User
import com.swoq.balance_app.repository.UserRepository
import com.swoq.balance_app.service.UserService
import java.math.BigDecimal
import java.security.SecureRandom
import java.util.UUID
import kotlin.system.measureTimeMillis
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.kotlin.core.publisher.toFlux
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@DataR2dbcTest
@Import(UserService::class)
class UserServiceTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private val random = SecureRandom()

    @BeforeEach
    fun setup() {
        val timeToEmptyMillis = measureTimeMillis {
            userRepository.deleteAll().block()
        }
        println("Emptied database in $timeToEmptyMillis ms")

        val random = SecureRandom()
        val users = (1..100000).map {
            User(
                name = UUID.randomUUID().toString(),
                balance = BigDecimal(random.nextDouble(1.0, 100000.0).toString())
            )
        }

        val timeToSaveMillis = measureTimeMillis {
            userRepository.saveAll(users.toFlux()).blockLast()
        }
        println("Saved 100,000 users in $timeToSaveMillis ms")
    }

    @Disabled("Enable to test performance")
    @Test
    fun `should update balance of 10,000 random users and record time`() {
        val users = userRepository.findAllPaged(10000, 0)

        val updatedUsers = users.map { it.copy(balance = BigDecimal(random.nextDouble(1.0, 100000.0).toString())) }

        val timeSpent = measureTimeMillis {
            StepVerifier.create(userRepository.saveAll(updatedUsers.toFlux()).then()).verifyComplete()
        }

        println("Time spent to update 10,000 users: $timeSpent ms")
    }
}
