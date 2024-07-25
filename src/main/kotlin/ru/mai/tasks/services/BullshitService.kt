package ru.mai.tasks.services

import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import ru.mai.tasks.models.dto.PortfolioRequest
import ru.mai.tasks.models.dto.ProjectRequest
import ru.mai.tasks.models.dto.ProtocolRequest
import ru.mai.tasks.models.dto.TaskRequest
import ru.mai.tasks.models.entity.tasks.*
import ru.mai.tasks.models.entity.user.User
import ru.mai.tasks.repository.*
import ru.mai.tasks.utils.WordUtils
import ru.mai.tasks.utils.exception.NotFoundException

/*
    Вот все это нужно разделить каждый класс на отдельный файл

    НИКОГДА ТАК НЕЛЬЗЯ ДЕЛАТЬ (1 день до дедлайна исполючение)
 */

@Service
class PortfolioService(private val portfolioRepository: PortfolioRepository) {
    fun createPortfolio(request: PortfolioRequest): Portfolio {
        val portfolio = Portfolio(name = request.name, tasks = mutableListOf())
        return portfolioRepository.save(portfolio)
    }

    fun getPortfolio(id: Long): Portfolio {
        return portfolioRepository.findById(id).orElseThrow { NotFoundException("Портфолио не найдено") }
    }

    fun getAllPortfolios(): List<Portfolio> = portfolioRepository.findAll()

    fun updatePortfolio(id: Long, request: PortfolioRequest): Portfolio {
        val portfolio = getPortfolio(id)
        portfolio.name = request.name
        return portfolioRepository.save(portfolio)
    }

    fun deletePortfolio(id: Long) {
        portfolioRepository.deleteById(id)
    }
}

@Service
class ProjectService(private val projectRepository: ProjectRepository) {
    fun createProject(request: ProjectRequest): Project {
        val project = Project(name = request.name, tasks = mutableListOf())
        return projectRepository.save(project)
    }

    fun getProject(id: Long): Project {
        return projectRepository.findById(id).orElseThrow { NotFoundException("Проект не найден") }
    }

    fun getAllProjects(): List<Project> = projectRepository.findAll()

    fun updateProject(id: Long, request: ProjectRequest): Project {
        val project = getProject(id)
        project.name = request.name
        return projectRepository.save(project)
    }

    fun deleteProject(id: Long) {
        projectRepository.deleteById(id)
    }
}

@Service
class ProtocolService(
    private val protocolRepository: ProtocolRepository,
    private val taskRepository: TaskRepository
) {
    fun createProtocol(request: ProtocolRequest): Protocol {
        val tasks = taskRepository.findAllById(request.taskIds).toMutableList()
        val protocol = Protocol(name = request.name, tasks = tasks)
        return protocolRepository.save(protocol)
    }

    fun getProtocol(id: Long): Protocol {
        return protocolRepository.findById(id).orElseThrow { NotFoundException("Протокол не найден") }
    }

    fun getAllProtocols(): List<Protocol> = protocolRepository.findAll()

    fun updateProtocol(id: Long, request: ProtocolRequest): Protocol {
        val protocol = getProtocol(id)
        val tasks = taskRepository.findAllById(request.taskIds).toMutableList()
        protocol.name = request.name
        protocol.tasks = tasks
        return protocolRepository.save(protocol)
    }

    fun deleteProtocol(id: Long) {
        protocolRepository.deleteById(id)
    }
}

@Service
class UserService(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {

    fun getUser(id: Long): User {
        return userRepository.findById(id).orElseThrow { NotFoundException("Пользователь не найден") }
    }

    fun getAllUsers(): List<User> = userRepository.findAll().toList()

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val portfolioRepository: PortfolioRepository,
    private val projectRepository: ProjectRepository,
    private val protocolRepository: ProtocolRepository,
    private val userRepository: UserRepository
) {
    fun createTask(request: TaskRequest): Task {
        val portfolio = portfolioRepository.findById(request.portfolioId)
            .orElseThrow { NotFoundException("Портфолио не найдено") }
        val project = projectRepository.findById(request.projectId)
            .orElseThrow { NotFoundException("Проект не найден") }
        val protocols = request.protocolIds.map {
            protocolRepository.findById(it).orElseThrow { NotFoundException("Протокол не найден")  }
        }
        val responsibles = request.responsibleIds.map {
            userRepository.findById(it).orElseThrow { NotFoundException("Пользователь не найден")  }
        }

        val task = Task(
            name = request.name,
            description = request.description,
            status = request.status,
            priority = request.priority,
            startDate = request.startDate,
            dueDate = request.dueDate,
            portfolio = portfolio,
            project = project,
            responsibles = responsibles.toMutableList(),
            protocol = protocols.toMutableList()
        )

        val savedTask = taskRepository.save(task)

        // Обновление связей в пользователях и протоколах
        responsibles.forEach { user ->
            user.tasks = user.tasks + savedTask
            userRepository.save(user)
        }

        protocols.forEach { protocol ->
            protocol.tasks = protocol.tasks + savedTask
            protocolRepository.save(protocol)
        }

        return savedTask
    }

    fun getTask(id: Long): Task {
        return taskRepository.findById(id).orElseThrow { NotFoundException("Задача не найдена") }
    }

    fun getAllTasks(): List<Task> = taskRepository.findAll().toList()

    fun getTasksByStatus(status: Status): List<Task> = taskRepository.findByStatus(status)

    fun getTasksByPriority(priority: Priority): List<Task> = taskRepository.findByPriority(priority)

    fun getTasksByPortfolio(portfolioId: Long): List<Task> {
        val portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow { NotFoundException("Портфолио не найдено") }
        return taskRepository.findByPortfolio(portfolio)
    }

    fun getTasksByProtocol(protocolId: List<Long>): List<Task> {
        val protocol = protocolId.map { protocolRepository.findById(it)
            .orElseThrow { NotFoundException("Протокол не найден") } }
        return taskRepository.findByProtocolIn(protocol)
    }

    fun getTasksByProject(projectId: Long): List<Task> {
        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Проект не найден") }
        return taskRepository.findByProject(project)
    }

    fun updateTask(id: Long, request: TaskRequest): Task {
        val task = getTask(id)
        task.name = request.name
        task.description = request.description
        task.status = request.status
        task.priority = request.priority
        return taskRepository.save(task)
    }

    fun deleteTask(id: Long) {
        taskRepository.deleteById(id)
    }

    fun filterTasks(status: Status?, priority: Priority?, portfolioId: Long?, projectId: Long?, protocolId: Long?, userId: Long?): List<Task> {
        val spec = Specification.where(null as Specification<Task>?)
            .and(TaskSpecification.hasStatus(status))
            .and(TaskSpecification.hasPriority(priority))
            .and(TaskSpecification.belongsToPortfolio(portfolioId))
            .and(TaskSpecification.belongsToProject(projectId))
            .and(TaskSpecification.belongsToProtocol(protocolId))
            .and(TaskSpecification.hasResponsible(userId))

        return taskRepository.findAll(spec)
    }

    fun generateReport(status: Status?, priority: Priority?, portfolioId: Long?, projectId: Long?, protocolId: Long?, userId: Long?, filePath: String) {
        val spec = Specification.where(null as Specification<Task>?)
            .and(TaskSpecification.hasStatus(status))
            .and(TaskSpecification.hasPriority(priority))
            .and(TaskSpecification.belongsToPortfolio(portfolioId))
            .and(TaskSpecification.belongsToProject(projectId))
            .and(TaskSpecification.belongsToProtocol(protocolId))
            .and(TaskSpecification.hasResponsible(userId))

        val tasks = taskRepository.findAll(spec)

        val wordUtils = WordUtils()
        wordUtils.createNewDocument()

        tasks.forEach {
            wordUtils.addTask(it.name, it.description, it.startDate, it.dueDate, it.priority, it.status, it.responsibles.map { it.fullName })
        }

        wordUtils.getDocument(filePath)
    }
}