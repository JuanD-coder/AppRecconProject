package com.rojasdev.apprecconproject.pdf

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rojasdev.apprecconproject.data.dataModel.ExcelCollectorReportData
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class ExcelGenerator(
    private val context: Context,
    private val data: List<ExcelCollectorReportData>,
    private val dateWeek: Pair<String, String>,
    var onFileCreated: () -> Unit
) {

    fun generate(uri: Uri) {

        try {
            val workbook: Workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Informe Semanal")

            val boldFont = workbook.createFont().apply { bold = true }
            val headerStyle = workbook.createCellStyle().apply {
                setFont(boldFont)
                alignment = HorizontalAlignment.CENTER
                fillForegroundColor = IndexedColors.LIGHT_GREEN.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                borderBottom = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

            val titleFont = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 16
            }

            val titleStyle = workbook.createCellStyle().apply {
                setFont(titleFont)
                alignment = HorizontalAlignment.CENTER
                fillForegroundColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                wrapText = true
            }

            val titleRow = sheet.createRow(0)
            titleRow.heightInPoints = 30f
            titleRow.createCell(0).apply {
                setCellValue("REPORTE SEMANAL de ${dateWeek.first} al ${dateWeek.second}")
                cellStyle = titleStyle
            }

            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 10))

            val headers = listOf(
                "No.", "Nombre", "Lunes", "Martes", "Miércoles",
                "Jueves", "Viernes", "Sábado", "Total KG", "Precio por KG", "Total a pagar"
            )

            val headerRow = sheet.createRow(1)
            headers.forEachIndexed { i, title ->
                val cell = headerRow.createCell(i)
                cell.setCellValue(title)
                cell.cellStyle = headerStyle
            }

            // Estilo normal
            val normalStyle = workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                borderBottom = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

            data.forEachIndexed { index, item ->
                val row = sheet.createRow(index + 2)
                var col = 0

                row.createCell(col++).apply {
                    setCellValue(item.id.toDouble())
                    cellStyle = normalStyle
                }

                row.createCell(col++).apply {
                    setCellValue(item.name)
                    cellStyle = normalStyle
                }

                val dias = listOf(
                    item.lunes, item.martes, item.miercoles,
                    item.jueves, item.viernes, item.sabado
                )

                dias.forEach { dia ->
                    row.createCell(col++).apply {
                        setCellValue(dia.toDouble())
                        cellStyle = normalStyle
                    }
                }

                row.createCell(col++).apply {
                    setCellValue(item.totalKg.toDouble())
                    cellStyle = normalStyle
                }

                row.createCell(col++).apply {
                    setCellValue(item.price.toDouble())
                    cellStyle = normalStyle
                }

                row.createCell(col++).apply {
                    val totalPrice = item.totalKg * item.price

                    setCellValue(totalPrice.toDouble())
                    cellStyle = normalStyle
                }
            }

            // Estilo de totales
            val totalStyle = workbook.createCellStyle().apply {
                setFont(boldFont)
                alignment = HorizontalAlignment.CENTER
                fillForegroundColor = IndexedColors.LIGHT_YELLOW.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                borderBottom = BorderStyle.THIN
                borderTop = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }

            val totalRow = sheet.createRow(data.size + 2)
            totalRow.createCell(1).apply {
                setCellValue("TOTAL GENERAL")
                cellStyle = totalStyle
            }

            val totales = listOf(
                data.sumOf { it.lunes },
                data.sumOf { it.martes },
                data.sumOf { it.miercoles },
                data.sumOf { it.jueves },
                data.sumOf { it.viernes },
                data.sumOf { it.sabado },
                data.sumOf { it.totalKg },
                data.firstOrNull()?.price ?: 0.0,
                data.sumOf { it.totalKg * it.price } // Total a pagar
            )


            totales.forEachIndexed { index, total ->
                totalRow.createCell(index + 2).apply {
                    setCellValue(total.toDouble())
                    cellStyle = totalStyle
                }
            }

            // Establecer manualmente los anchos de columnas (en caracteres * 256)
            val columnWidths = listOf(
                10, 20, 12, 12, 12, 12, 12, 12, 15, 15, 18
            )

            columnWidths.forEachIndexed { index, widthInChars ->
                sheet.setColumnWidth(index, widthInChars * 256)
            }

            // Guardar
            context.contentResolver.openOutputStream(uri)?.use { out: OutputStream ->
                workbook.write(out)
                out.flush()
            }

            workbook.close()
            onFileCreated()

        } catch (e: Exception) {
            Log.e("ExcelGenerator", "Error generando Excel", e)
        }
    }
}
