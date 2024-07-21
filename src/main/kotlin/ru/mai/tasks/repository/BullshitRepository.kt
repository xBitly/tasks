package ru.mai.tasks.repository

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mai.tasks.models.entity.tasks.*
import ru.mai.tasks.models.entity.user.User


/*
    Вот все это нужно разделить каждый интерфейс на отдельный файл
    + поменять JpaRepository на CrudRepository

    НИКОГДА ТАК НЕЛЬЗЯ ДЕЛАТЬ (1 день до дедлайна исполючение)
 */

@Repository
interface PortfolioRepository: JpaRepository<Portfolio, Long>

@Repository
interface ProjectRepository : JpaRepository<Project, Long>

@Repository
interface ProtocolRepository : JpaRepository<Protocol, Long>

@Repository
interface TaskRepository : CrudRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    fun findByStatus(status: Status): List<Task>
    fun findByPriority(priority: Priority): List<Task>
    fun findByPortfolio(portfolio: Portfolio): List<Task>
    fun findByProtocolIn(protocol: List<Protocol>): List<Task>
    fun findByProject(project: Project): List<Task>
}

class TaskSpecification {

    companion object {
        fun hasStatus(status: Status?): Specification<Task>? {
            return if (status == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                builder.equal(root.get<Status>("status"), status)
            }
        }

        fun hasPriority(priority: Priority?): Specification<Task>? {
            return if (priority == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                builder.equal(root.get<Priority>("priority"), priority)
            }
        }

        fun hasResponsible(userId: Long?): Specification<Task>? {
            return if (userId == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                val join = root.join<Task, User>("responsibles")
                builder.equal(join.get<Long>("id"), userId)
            }
        }

        fun belongsToPortfolio(portfolioId: Long?): Specification<Task>? {
            return if (portfolioId == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                builder.equal(root.get<Portfolio>("portfolio").get<Long>("id"), portfolioId)
            }
        }

        fun belongsToProject(projectId: Long?): Specification<Task>? {
            return if (projectId == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                builder.equal(root.get<Project>("project").get<Long>("id"), projectId)
            }
        }

        fun belongsToProtocol(protocolId: Long?): Specification<Task>? {
            return if (protocolId == null) {
                null
            } else Specification { root: Root<Task>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
                val join = root.join<Task, Protocol>("protocol")
                builder.equal(join.get<Long>("id"), protocolId)
            }
        }
    }
}