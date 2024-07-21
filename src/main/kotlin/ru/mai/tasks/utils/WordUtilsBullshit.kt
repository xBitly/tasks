package ru.mai.tasks.utils

import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import ru.mai.tasks.models.entity.tasks.Priority
import ru.mai.tasks.models.entity.tasks.Status
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate

class WordUtils(private val title: String, private val margin: Int, private val spacing: Double) {
    private val document: XWPFDocument = XWPFDocument()

    init {
        val margins = document.document.body.addNewSectPr().addNewPgMar()
        margins.left = (margin * 20).toBigInteger()
        margins.right = (margin * 20).toBigInteger()
        margins.top = (margin * 20).toBigInteger()
        margins.bottom = (margin * 20).toBigInteger()
    }

    fun setText(
        alignment: ParagraphAlignment,
        color: String,
        fontFamily: String,
        isBold: Boolean,
        fontSize: Int,
        text: String
    ) {
        val paragraph = document.createParagraph()
        paragraph.alignment = alignment
        paragraph.spacingBetween = spacing

        val run = paragraph.createRun()
        run.setText(text)
        run.fontFamily = fontFamily
        run.isBold = isBold
        run.fontSize = fontSize
        run.setColor(color)
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
        setText(ParagraphAlignment.LEFT, "000000", "Arial", true, 12, "Задача: $taskName")
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Описание: $description")
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Дата начала: $startDate")
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Дата окончания: $dueDate")
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Приоритет: ${priority.value}")
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Статус: ${status.name}")
        if (responsibles.isNotEmpty()) {
            setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "Ответственные: ${responsibles.joinToString(", ")}")
        }
        setText(ParagraphAlignment.LEFT, "000000", "Arial", false, 12, "")
    }

    fun getDocument(filePath: String) {
        FileOutputStream(filePath).use { outputStream ->
            document.write(outputStream)
        }
    }
    fun readDocument(filePath: String) {
        FileInputStream(filePath).use { inputStream ->
            val doc = XWPFDocument(inputStream)
            for (para in doc.paragraphs) {
                println(para.text)
            }
        }
    }
}