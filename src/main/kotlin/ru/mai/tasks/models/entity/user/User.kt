package ru.mai.tasks.models.entity.user

import jakarta.persistence.*
import ru.mai.tasks.models.entity.AbstractEntity
import ru.mai.tasks.models.entity.tasks.Task

@Entity
@Table(name = "users")
class User(
    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "phone", nullable = false)
    var phone: String,

    @Column(name = "full_name", nullable = false)
    var fullName: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "role", nullable = false)
    var role: Role = Role.USER,

    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_task",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "task_id")]
    )
    var tasks: List<Task>
) : AbstractEntity() {
}