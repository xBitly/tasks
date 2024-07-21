package ru.mai.tasks.models.entity.token

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import ru.mai.tasks.models.entity.AbstractEntity

@Entity
@Table(name = "tokens")
class Token(
    @Column(name = "user_id")
    var userId: Long,

    @Column(name = "access_token")
    var accessToken: String,

    @Column(name = "refresh_token")
    var refreshToken: String
) : AbstractEntity()