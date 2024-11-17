package com.example.attendance

import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ExcelUtils {



    fun writeDataToExcel(sheetName: String, data: List<List<String>>,filename:String) {


        try {
            // Create a Workbook
            val workbook = XSSFWorkbook()


            // Create a Sheet //regex is used to replace / in date with - bcuz excel doesnt support /?[]etc in sheetname
            val sheet = workbook.createSheet(sheetName.replace(Regex("[/\\\\?*\\[\\]]"), "-"))

            // Create Header Row
            val headerRow: Row = sheet.createRow(0)
            val headers = listOf("Name", "Attendance")
            for ((index, header) in headers.withIndex()) {
                val cell: Cell = headerRow.createCell(index)
                cell.setCellValue(header)
            }







            data.toList()//To make the list work in the down forloop
            Log.i("jwn",data.toString())




            for ((rowIndex, rowData) in data.withIndex()) {
                val row: Row = sheet.createRow(rowIndex + 1)
                for ((cellIndex, cellData) in rowData.withIndex()) {
                    val cell: Cell = row.createCell(cellIndex)
                    cell.setCellValue(cellData)
                }
            }

            // Create a File in External Storage
            val fileName = "$filename.xlsx"
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)

            // Write the Workbook to File
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)

            // Close Streams
            fileOutputStream.close()
            workbook.close()

            println("Excel file created successfully at: ${file.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            println("Error creating Excel file: ${e.message}")
            Log.i("mrr",e.message.toString())
        }








    }

}