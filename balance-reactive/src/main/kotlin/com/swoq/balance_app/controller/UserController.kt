package com.swoq.balance_app.controller

import com.swoq.balance_app.data.User
import com.swoq.balance_app.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/balance")
    fun setUsersBalance(@RequestBody balances: Map<Int, Int>): Mono<String> {
        return userService.updateUserBalances(balances)
    }

    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Flux<User> = userService.getUsers(page, size)
}
