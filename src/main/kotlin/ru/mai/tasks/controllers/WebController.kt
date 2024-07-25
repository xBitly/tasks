package ru.mai.tasks.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @GetMapping("/auth")
    fun showSignIn(): String {
        return "launcher"
    }

    @GetMapping("/tasks")
    fun showTasks(): String {
        return "tasks"
    }

    @GetMapping("/users")
    fun showUsers(): String {
        return "users"
    }

    @GetMapping("/projects")
    fun showProjects(): String {
        return "projects"
    }

    @GetMapping("/portfolios")
    fun showPortfolios(): String {
        return "portfolios"
    }

    @GetMapping("/protocols")
    fun showProtocols(): String {
        return "protocols"
    }
}