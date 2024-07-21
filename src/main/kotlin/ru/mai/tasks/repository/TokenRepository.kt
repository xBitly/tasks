package ru.mai.tasks.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mai.tasks.models.entity.token.Token

@Repository
interface TokenRepository : CrudRepository<Token, Long> {
    fun getTokenByUserId(userId: Long): Token?
    fun removeTokenByUserId(userId: Long)
}