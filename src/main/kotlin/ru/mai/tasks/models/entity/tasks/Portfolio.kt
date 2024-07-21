package ru.mai.tasks.models.entity.tasks

import jakarta.persistence.*
import ru.mai.tasks.models.entity.AbstractEntity

@Entity
@Table(name = "portfolios")
class Portfolio(
    @Column(name = "name", nullable = false)
    var name: String,

    @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
    val tasks: MutableList<Task>
) : AbstractEntity() {
}