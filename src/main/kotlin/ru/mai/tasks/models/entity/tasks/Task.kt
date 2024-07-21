package ru.mai.tasks.models.entity.tasks

import jakarta.persistence.*
import ru.mai.tasks.models.entity.AbstractEntity
import ru.mai.tasks.models.entity.user.User
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
class Task(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description", nullable = false, length = 1023)
    var description: String,

    @Column(name = "status", nullable = false)
    var status: Status = Status.OPEN,

    @Column(name = "priority", nullable = false)
    var priority: Priority,

    @Column(name = "start_date", updatable = false)
    val startDate: LocalDate,

    @Column(name = "due_date", updatable = false)
    val dueDate: LocalDate,

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    var responsibles: MutableList<User>,

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    var protocol: MutableList<Protocol>,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id", nullable = false)
    val portfolio: Portfolio,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    val project: Project
) : AbstractEntity() {
}