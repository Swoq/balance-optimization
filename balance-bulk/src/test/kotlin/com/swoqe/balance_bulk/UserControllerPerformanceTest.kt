package com.swoqe.balance_bulk

import com.swoqe.balance_bulk.data.User
import com.swoqe.balance_bulk.repository.UserRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import java.math.BigDecimal
import java.security.SecureRandom
import java.util.UUID
import kotlin.system.measureTimeMillis
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerPerformanceTest {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var userRepository: UserRepository

    val random = SecureRandom()

    @BeforeEach
    fun setup() {
        val timeToEmptyMillis = measureTimeMillis {
            userRepository.deleteAll()
        }
        println("Emptied database in $timeToEmptyMillis ms")

        val users = (1..100000).map {
            User(
                name = UUID.randomUUID().toString(),
                balance = BigDecimal(random.nextDouble(1.0, 100000.0).toString())
            )
        }
        val timeToSaveMillis = measureTimeMillis {
            userRepository.saveAll(users)
        }

        println("Saved 100,000 users in $timeToSaveMillis ms")
    }

    @Test
    fun `update 10k records and measure the time`() {
        val savedUsers = userRepository.findAllPaged(10000, 0).toList()

        savedUsers.forEach {
            it.balance = BigDecimal(random.nextDouble(1.0, 100000.0).toString())
        }
        val balanceDto: Map<Long, BigDecimal> = savedUsers.associateBy({ it.id!! }, { it.balance })

        val timeToUpdateMillis = measureTimeMillis {
            RestAssured.given().port(port)
                .contentType(ContentType.JSON)
                .body(balanceDto)
                .post("/api/users/balance")
                .then()
                .statusCode(200)
                .body(equalTo("Users updated successfully"))
        }
        println("Time spent to update 10,000 users: $timeToUpdateMillis ms")
    }

}
