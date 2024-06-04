package com.swoqe.balance_bulk.controller

import com.swoqe.balance_bulk.data.BalanceDto
import com.swoqe.balance_bulk.data.User
import com.swoqe.balance_bulk.service.UserService
import java.math.BigDecimal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/balance")
    fun saveUsers(@RequestBody balances: Map<Long, BigDecimal>): ResponseEntity<String> {
        val listedBalances = balances.entries.map { (id, balance) -> BalanceDto(id, balance) }.toList()
        userService.saveUsersInBatches(listedBalances)
        return ResponseEntity("Users updated successfully", HttpStatus.OK)
    }

    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<User>> = ResponseEntity.ok(userService.getUsers(page, size))
}
