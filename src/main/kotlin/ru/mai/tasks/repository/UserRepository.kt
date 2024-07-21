package ru.mai.tasks.repository

import ru.mai.tasks.models.entity.user.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun getUserById(id: Long): User?
    fun getUserByEmail(email: String): User?
    fun removeUserById(id: Long)
}