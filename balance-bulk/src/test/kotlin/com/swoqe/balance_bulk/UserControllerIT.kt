package com.swoqe.balance_bulk

import com.swoqe.balance_bulk.data.User
import com.swoqe.balance_bulk.repository.UserRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import java.math.BigDecimal
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
class UserControllerIT {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `should update user balances`() {
        val savedUsers = userRepository.saveAll(
            listOf(
                User(name = "Alice", balance = BigDecimal(0)),
                User(name = "Bob", balance = BigDecimal(0)),
                User(name = "Charlie", balance = BigDecimal(0))
            )
        ).toList()

        val balances: Map<Long, BigDecimal> = mapOf(
            savedUsers[0].id!! to BigDecimal(1000),
            savedUsers[1].id!! to BigDecimal(2000)
        )

        RestAssured.given().port(port)
            .contentType(ContentType.JSON)
            .body(balances)
            .post("/api/users/balance")
            .then()
            .statusCode(200)
            .body(equalTo("Users updated successfully"))

        RestAssured.given().port(port)
            .get("/api/users?page=0&size=10")
            .then()
            .statusCode(200)
            .body("find { it.id == ${savedUsers[0].id} }.balance", equalTo(1000.0F))
            .body("find { it.id == ${savedUsers[1].id} }.balance", equalTo(2000.0F))
            .body("find { it.id == ${savedUsers[2].id} }.balance", equalTo(0.0F))
    }

    @Test
    fun `should return bad request for invalid Id`() {
        val invalidBalancesId = mapOf("invalidKey" to 100)

        RestAssured.given().port(port)
            .contentType(ContentType.JSON)
            .body(invalidBalancesId)
            .post("/api/users/balance")
            .then()
            .statusCode(400)
    }

    @Test
    fun `should return bad request for invalid balance`() {
        val invalidBalance = mapOf(2L to "invalidBalance")

        RestAssured.given().port(port)
            .contentType(ContentType.JSON)
            .body(invalidBalance)
            .post("/api/users/balance")
            .then()
            .statusCode(400)
    }
}
