package ru.mai.tasks.controllers

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ru.mai.tasks.models.dto.*
import ru.mai.tasks.models.entity.tasks.Priority
import ru.mai.tasks.models.entity.tasks.Status
import ru.mai.tasks.services.*
import java.io.File

@RestController
@RequestMapping("/api/portfolios")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
class PortfolioController(private val portfolioService: PortfolioService) {
    @PostMapping
    fun createPortfolio(@RequestBody request: PortfolioRequest, auth: Authentication): PortfolioResponse {
        val portfolio = portfolioService.createPortfolio(request)
        return PortfolioResponse(portfolio.id, portfolio.name)
    }

    @GetMapping("/{id}")
    fun getPortfolio(@PathVariable id: Long, auth: Authentication): PortfolioResponse {
        val portfolio = portfolioService.getPortfolio(id)
        return PortfolioResponse(portfolio.id, portfolio.name)
    }

    @GetMapping
    fun getAllPortfolios(auth: Authentication): List<PortfolioResponse> {
        return portfolioService.getAllPortfolios().map { PortfolioResponse(it.id, it.name) }
    }

    @PutMapping("/{id}")
    fun updatePortfolio(@PathVariable id: Long, @RequestBody request: PortfolioRequest, auth: Authentication): PortfolioResponse {
        val portfolio = portfolioService.updatePortfolio(id, request)
        return PortfolioResponse(portfolio.id, portfolio.name)
    }

    @DeleteMapping("/{id}")
    fun deletePortfolio(@PathVariable id: Long, auth: Authentication) {
        portfolioService.deletePortfolio(id)
    }
}

@RestController
@RequestMapping("/api/projects")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
class ProjectController(private val projectService: ProjectService) {
    @PostMapping
    fun createProject(@RequestBody request: ProjectRequest, auth: Authentication): ProjectResponse {
        val project = projectService.createProject(request)
        return ProjectResponse(project.id, project.name)
    }

    @GetMapping("/{id}")
    fun getProject(@PathVariable id: Long, auth: Authentication): ProjectResponse {
        val project = projectService.getProject(id)
        return ProjectResponse(project.id, project.name)
    }

    @GetMapping
    fun getAllProjects(auth: Authentication): List<ProjectResponse> {
        return projectService.getAllProjects().map { ProjectResponse(it.id, it.name) }
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: Long, @RequestBody request: ProjectRequest, auth: Authentication): ProjectResponse {
        val project = projectService.updateProject(id, request)
        return ProjectResponse(project.id, project.name)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Long, auth: Authentication) {
        projectService.deleteProject(id)
    }
}

@RestController
@RequestMapping("/api/protocols")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
class ProtocolController(private val protocolService: ProtocolService) {
    @PostMapping
    fun createProtocol(@RequestBody request: ProtocolRequest, auth: Authentication): ProtocolResponse {
        val protocol = protocolService.createProtocol(request)
        return ProtocolResponse(protocol.id, protocol.name, protocol.tasks.map { it.toTaskResponse() })
    }

    @GetMapping("/{id}")
    fun getProtocol(@PathVariable id: Long, auth: Authentication): ProtocolResponse {
        val protocol = protocolService.getProtocol(id)
        return ProtocolResponse(protocol.id, protocol.name, protocol.tasks.map { it.toTaskResponse() })
    }

    @GetMapping
    fun getAllProtocols(auth: Authentication): List<ProtocolResponse> {
        return protocolService.getAllProtocols().map { ProtocolResponse(it.id, it.name, it.tasks.map { it.toTaskResponse() }) }
    }

    @PutMapping("/{id}")
    fun updateProtocol(@PathVariable id: Long, @RequestBody request: ProtocolRequest, auth: Authentication): ProtocolResponse {
        val protocol = protocolService.updateProtocol(id, request)
        return ProtocolResponse(protocol.id, protocol.name, protocol.tasks.map { it.toTaskResponse() })
    }

    @DeleteMapping("/{id}")
    fun deleteProtocol(@PathVariable id: Long, auth: Authentication) {
        protocolService.deleteProtocol(id)
    }
}

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
class TaskController(private val taskService: TaskService) {

    @PostMapping
    fun createTask(@RequestBody request: TaskRequest, auth: Authentication): TaskResponse {
        val task = taskService.createTask(request)
        return TaskResponse(
            task.id, task.name, task.description, task.status, task.priority,
            task.startDate, task.dueDate,
            task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
            task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
        )
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long, auth: Authentication): TaskResponse {
        val task = taskService.getTask(id)
        return TaskResponse(
            task.id, task.name, task.description, task.status, task.priority,
            task.startDate, task.dueDate,
            task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
            task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
        )
    }

    @GetMapping
    fun getAllTasks(auth: Authentication): List<TaskResponse> {
        return taskService.getAllTasks().map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @GetMapping("/status/{status}")
    fun getTasksByStatus(@PathVariable status: Status, auth: Authentication): List<TaskResponse> {
        return taskService.getTasksByStatus(status).map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @GetMapping("/priority/{priority}")
    fun getTasksByPriority(@PathVariable priority: Priority, auth: Authentication): List<TaskResponse> {
        return taskService.getTasksByPriority(priority).map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @GetMapping("/portfolio/{portfolioId}")
    fun getTasksByPortfolio(@PathVariable portfolioId: Long, auth: Authentication): List<TaskResponse> {
        return taskService.getTasksByPortfolio(portfolioId).map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @GetMapping("/protocol")
    fun getTasksByProtocol(@RequestParam protocolId: List<Long>, auth: Authentication): List<TaskResponse> {
        return taskService.getTasksByProtocol(protocolId).map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @GetMapping("/project/{projectId}")
    fun getTasksByProject(@PathVariable projectId: Long, auth: Authentication): List<TaskResponse> {
        return taskService.getTasksByProject(projectId).map { task ->
            TaskResponse(
                task.id, task.name, task.description, task.status, task.priority,
                task.startDate, task.dueDate,
                task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
            )
        }
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody request: TaskRequest, auth: Authentication): TaskResponse {
        val task = taskService.updateTask(id, request)
        return TaskResponse(
            task.id, task.name, task.description, task.status, task.priority,
            task.startDate, task.dueDate,
            task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
            task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long, auth: Authentication) {
        taskService.deleteTask(id)
    }

    @GetMapping("/filter")
    fun filterTasks(
        @RequestParam(required = false) status: Status?,
        @RequestParam(required = false) priority: Priority?,
        @RequestParam(required = false) portfolioId: Long?,
        @RequestParam(required = false) projectId: Long?,
        @RequestParam(required = false) protocolId: List<Long>?,
        @RequestParam(required = false) userId: Long?,
        auth: Authentication
    ): List<TaskResponse> {
        return taskService.filterTasks(status, priority, portfolioId, projectId, protocolId?.first(), userId)
            .map { task ->
                TaskResponse(
                    task.id, task.name, task.description, task.status, task.priority,
                    task.startDate, task.dueDate,
                    task.portfolio.toPortfolioResponse(), task.project.toProjectResponse(),
                    task.protocol.map { it.toProtocolShortResponse() }, task.responsibles.map { it.toUserResponse() }
                )
            }
    }
}

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
class ReportController(private val reportService: ReportService) {

    @GetMapping("/user/{userId}")
    fun generateUserReport(@PathVariable userId: Long, auth: Authentication): ResponseEntity<ByteArrayResource> {
        val filePath = "user_report_$userId.docx"
        reportService.generateUserReport(userId, filePath)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filePath\"")
            .body(ByteArrayResource(File(filePath).readBytes()))
    }

    @GetMapping("/project/{projectId}")
    fun generateProjectReport(@PathVariable projectId: Long, auth: Authentication): ResponseEntity<ByteArrayResource> {
        val filePath = "project_report_$projectId.docx"
        reportService.generateProjectReport(projectId, filePath)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filePath\"")
            .body(ByteArrayResource(File(filePath).readBytes()))
    }

    @GetMapping("/portfolio/{portfolioId}")
    fun generatePortfolioReport(@PathVariable portfolioId: Long, auth: Authentication): ResponseEntity<ByteArrayResource> {
        val filePath = "portfolio_report_$portfolioId.docx"
        reportService.generatePortfolioReport(portfolioId, filePath)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filePath\"")
            .body(ByteArrayResource(File(filePath).readBytes()))
    }

    @GetMapping("/protocol/{protocolId}")
    fun generateProtocolReport(@PathVariable protocolId: Long, auth: Authentication): ResponseEntity<ByteArrayResource> {
        val filePath = "protocol_report_$protocolId.docx"
        reportService.generateProtocolReport(protocolId, filePath)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filePath\"")
            .body(ByteArrayResource(File(filePath).readBytes()))
    }
}
