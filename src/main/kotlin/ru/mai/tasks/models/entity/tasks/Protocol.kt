package ru.mai.tasks.models.entity.tasks

import jakarta.persistence.*
import ru.mai.tasks.models.entity.AbstractEntity

@Entity
@Table(name = "protocols")
class Protocol(
    @Column(name = "name", nullable = false)
    var name: String,

    @ManyToMany(cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
    @JoinTable(
        name = "protocol_task",
        joinColumns = [JoinColumn(name = "protocol_id")],
        inverseJoinColumns = [JoinColumn(name = "task_id")]
    )
    var tasks: List<Task>
) : AbstractEntity() {
}