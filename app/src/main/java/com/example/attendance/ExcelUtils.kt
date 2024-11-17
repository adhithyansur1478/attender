package com.example.attendance

import android.os.Environment
import android.os.storage.StorageManager
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ExcelUtils {



    fun writeDataToExcel(sheetName: String, data: List<List<String>>) {
        // Create a new workbook and sheet
        val workbook: Workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(sheetName)

        // Write data to the sheet
        for (i in data.indices) {
            val row = sheet.createRow(i)
            for (j in data[i].indices) {
                val cell = row.createCell(j)
                cell.setCellValue(data[i][j])
            }
        }






    }

}