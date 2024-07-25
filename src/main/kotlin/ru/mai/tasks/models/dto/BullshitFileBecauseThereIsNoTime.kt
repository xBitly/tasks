package ru.mai.tasks.models.dto

import ru.mai.tasks.models.entity.tasks.*
import ru.mai.tasks.models.entity.user.Role
import ru.mai.tasks.models.entity.user.User
import java.time.LocalDate

/*
    Вот все это нужно нормально раскидать по request/response разделив каждый класс на отдельный файл
    + написать к ним мапперы

    НИКОГДА ТАК НЕЛЬЗЯ ДЕЛАТЬ (1 день до дедлайна исполючение)
 */
data class PortfolioRequest(
    val name: String
)

data class PortfolioResponse(
    val id: Long,
    val name: String
)

data class ProjectRequest(
    val name: String
)

data class ProjectResponse(
    val id: Long,
    val name: String
)

data class ProtocolRequest(
    val name: String,
    val taskIds: List<Long>
)

data class ProtocolResponse(
    val id: Long,
    val name: String,
)

data class ProtocolShortResponse(
    val id: Long,
    val name: String
)

data class TaskRequest(
    val name: String,
    val description: String,
    val status: Status,
    val priority: Priority,
    val startDate: LocalDate,
    val dueDate: LocalDate,
    val portfolioId: Long,
    val projectId: Long,
    val protocolIds: List<Long>,
    val responsibleIds: List<Long>
)

data class TaskResponse(
    val id: Long,
    val name: String,
    val description: String,
    val status: Status,
    val priority: Priority,
    val startDate: LocalDate,
    val dueDate: LocalDate,
    val portfolio: PortfolioResponse,
    val project: ProjectResponse,
    val protocols: List<ProtocolShortResponse>,
    val responsible: List<UserResponse>
)

data class UserResponse(
    val id: Long,
    val email: String,
    val phone: String,
    val fullName: String,
    val role: Role
)


// Преобразование Portfolio в PortfolioResponse
fun Portfolio.toPortfolioResponse(): PortfolioResponse {
    return PortfolioResponse(
        id = this.id,
        name = this.name
    )
}

// Преобразование Project в ProjectResponse
fun Project.toProjectResponse(): ProjectResponse {
    return ProjectResponse(
        id = this.id,
        name = this.name
    )
}

// Преобразование Protocol в ProtocolResponse
fun Protocol.toProtocolResponse(): ProtocolResponse {
    return ProtocolResponse(
        id = this.id,
        name = this.name,
    )
}

fun Protocol.toProtocolShortResponse(): ProtocolShortResponse {
    return ProtocolShortResponse(
        id = this.id,
        name = this.name
    )
}

// Преобразование User в UserResponse
fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        email = this.email,
        phone = this.phone,
        fullName = this.fullName,
        role = this.role
    )
}

// Преобразование Task в TaskResponse
fun Task.toTaskResponse(): TaskResponse {
    return TaskResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        status = this.status,
        priority = this.priority,
        startDate = this.startDate,
        dueDate = this.dueDate,
        portfolio = this.portfolio.toPortfolioResponse(),
        project = this.project.toProjectResponse(),
        protocols = this.protocol.map { it.toProtocolShortResponse() },
        responsible = this.responsibles.map { it.toUserResponse() }
    )
}