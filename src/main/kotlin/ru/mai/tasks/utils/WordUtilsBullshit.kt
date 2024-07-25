package ru.mai.tasks.utils

import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import ru.mai.tasks.models.entity.tasks.Priority
import ru.mai.tasks.models.entity.tasks.Status
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate

class WordUtils {
    private var document: XWPFDocument? = null

    // Создание нового документа
    fun createNewDocument() {
        document = XWPFDocument()
    }

    // Добавление текста в документ
    fun setText(text: String) {
        val paragraph: XWPFParagraph = document!!.createParagraph()
        val run: XWPFRun = paragraph.createRun()
        run.setText(text)
    }

    fun addTask(
        taskName: String,
        description: String,
        startDate: LocalDate,
        dueDate: LocalDate,
        priority: Priority,
        status: Status,
        responsibles: List<String> = emptyList()
    ) {
        setText("Задача: $taskName")
        setText("Описание: $description")
        setText("Дата начала: $startDate")
        setText("Дата окончания: $dueDate")
        setText("Приоритет: ${priority.value}")
        setText("Статус: ${status.name}")
        if (responsibles.isNotEmpty()) {
            setText("Ответственные: ${responsibles.joinToString(", ")}")
        }
        setText("")
    }

    // Сохранение документа
    fun getDocument(filePath: String) {
        FileOutputStream(filePath).use { outputStream ->
            document!!.write(outputStream)
        }
    }

    // Чтение документа
    fun readDocument(filePath: String): String {
        val inputStream = FileInputStream(filePath)
        document = XWPFDocument(inputStream)
        val paragraphs = document!!.paragraphs
        val textBuilder = StringBuilder()
        for (paragraph in paragraphs) {
            textBuilder.append(paragraph.text).append("\n")
        }
        return textBuilder.toString()
    }

    // Закрытие документа
    fun closeDocument() {
        document?.close()
    }
}