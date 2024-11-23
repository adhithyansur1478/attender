package com.example.attendance

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ExcelUtils(context: Context) {


    var cont:Context = context
    fun writeDataToExcel(sheetName: String, data: List<List<String>>, filename: String,progressBar:Dialog) {


        fun workbookExists(filePath: String): Boolean {
            val file = File("/storage/emulated/0/Download/$filePath")
            return file.exists() // Check if the file exists
        }

        if (workbookExists("$filename.xlsx")) {
            Log.i("ext", "done")
            // Step 1: Open an existing workbook

            val fileIn =
                FileInputStream("/storage/emulated/0/Download/$filename.xlsx") // Path to your existing file




            CoroutineScope(Dispatchers.IO).launch {

                withContext(Dispatchers.Main) {
                    progressBar.show()
                }

                try {
                    val workbook = XSSFWorkbook(fileIn) // This will load the workbook

                    // Create a Sheet //regex is used to replace / in date with - bcuz excel doesnt support /?[]etc in sheetname
                    val sheet =
                        workbook.createSheet(sheetName.replace(Regex("[/\\\\?*\\[\\]]"), "-"))

                    // Create Header Row
                    val headerRow: Row = sheet.createRow(0)
                    val headers = listOf("Name", "Attendance")
                    for ((index, header) in headers.withIndex()) {
                        val cell: Cell = headerRow.createCell(index)
                        cell.setCellValue(header)
                    }



                    data.toList()//To make the list work in the down forloop
                    Log.i("jwn", data.toString())


                    for ((rowIndex, rowData) in data.withIndex()) {
                        val row: Row = sheet.createRow(rowIndex + 1)
                        for ((cellIndex, cellData) in rowData.withIndex()) {
                            val cell: Cell = row.createCell(cellIndex)
                            cell.setCellValue(cellData)
                        }
                    }

                    // Create a File in External Storage
                    val fileName = "$filename.xlsx"
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        fileName
                    )

                    // Write the Workbook to File
                    val fileOutputStream = FileOutputStream(file)
                    workbook.write(fileOutputStream)

                    // Close Streams
                    fileOutputStream.close()
                    workbook.close()

                    println("Excel file created successfully at: ${file.absolutePath}")

                    // Dismiss the progress bar when the task is completed (on the main thread)
                    withContext(Dispatchers.Main) {
                        progressBar.dismiss()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error creating Excel file: ${e.message}")
                    Log.i("mrr", e.message.toString())
                    // Dismiss the progress bar and show error message (on the main thread)
                    withContext(Dispatchers.Main) {

                        Toast.makeText(cont, e.message.toString(), Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()

                    }

                }
            }
        } else {
            Log.i("ext", "nop")
            progressBar.show()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Create a Workbook
                    val workbook = XSSFWorkbook()


                    // Create a Sheet //regex is used to replace / in date with - bcuz excel doesnt support /?[]etc in sheetname
                    val sheet =
                        workbook.createSheet(sheetName.replace(Regex("[/\\\\?*\\[\\]]"), "-"))

                    // Create Header Row
                    val headerRow: Row = sheet.createRow(0)
                    val headers = listOf("Name", "Attendance")
                    for ((index, header) in headers.withIndex()) {
                        val cell: Cell = headerRow.createCell(index)
                        cell.setCellValue(header)
                    }







                    data.toList()//To make the list work in the down forloop
                    Log.i("jwn", data.toString())




                    for ((rowIndex, rowData) in data.withIndex()) {
                        val row: Row = sheet.createRow(rowIndex + 1)
                        for ((cellIndex, cellData) in rowData.withIndex()) {
                            val cell: Cell = row.createCell(cellIndex)
                            cell.setCellValue(cellData)
                        }
                    }

                    // Create a ContentValues object to insert data into MediaStore
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)  // File name
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")  // Store in Downloads directory
                    }

                    // Insert file into MediaStore
                    val uri: Uri? = cont.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                    Log.i("ur1434",uri.toString())
                    if (uri != null) {
                        Log.i("ur143",uri.path.toString().toUri().toString())
                    }

                    // Open output stream to write to the file
                    uri?.let { fileUri ->
                        val outputStream: OutputStream? = cont.contentResolver.openOutputStream(fileUri)
                        outputStream?.let { stream ->
                            // Write workbook to output stream
                            workbook.write(stream)

                            stream.close()
                            workbook.close()

                            withContext(Dispatchers.Main) {

                                Toast.makeText(cont, "Excel file saved to Downloads.", Toast.LENGTH_LONG).show()
                            }


                        }
                    }

                    // Dismiss the progress bar when the task is completed (on the main thread)
                    withContext(Dispatchers.Main) {
                        progressBar.dismiss()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error creating Excel file: ${e.message}")
                    Log.i("mrr", e.message.toString())
                    // Dismiss the progress bar and show error message (on the main thread)
                    withContext(Dispatchers.Main) {

                        Toast.makeText(cont, e.message.toString(), Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()

                    }
                }
            }

        }
    }



    }

