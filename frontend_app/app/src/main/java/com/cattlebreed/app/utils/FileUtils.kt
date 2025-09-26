package com.cattlebreed.app.utils

import android.content.Context
import android.os.Environment
import com.cattlebreed.app.data.entity.AnimalRecord
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class FileUtils(private val context: Context) {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    
    fun createImageFile(): File {
        val timeStamp = dateFormat.format(Date())
        val imageFileName = "CATTLE_${timeStamp}"
        val storageDir = File(context.filesDir, "images")
        
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        
        return File(storageDir, "${imageFileName}.jpg")
    }
    
    fun exportToJson(records: List<AnimalRecord>): File {
        val timeStamp = dateFormat.format(Date())
        val fileName = "cattle_records_${timeStamp}.json"
        val exportDir = File(context.filesDir, "exports")
        
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        
        val file = File(exportDir, fileName)
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setPrettyPrinting()
            .create()
        
        val jsonData = gson.toJson(records)
        
        FileWriter(file).use { writer ->
            writer.write(jsonData)
        }
        
        return file
    }
    
    fun exportToCsv(records: List<AnimalRecord>): File {
        val timeStamp = dateFormat.format(Date())
        val fileName = "cattle_records_${timeStamp}.csv"
        val exportDir = File(context.filesDir, "exports")
        
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        
        val file = File(exportDir, fileName)
        
        FileWriter(file).use { writer ->
            // Write CSV header
            writer.write("ID,Animal ID,Date,Image Path,Body Length,Height,Chest Width,Rump Angle,ATC Score,Synced\n")
            
            // Write data rows
            records.forEach { record ->
                writer.write("${record.id},")
                writer.write("${record.animalId},")
                writer.write("${displayDateFormat.format(record.date)},")
                writer.write("${record.imagePath},")
                writer.write("${record.bodyLength},")
                writer.write("${record.height},")
                writer.write("${record.chestWidth},")
                writer.write("${record.rumpAngle},")
                writer.write("${record.atcScore},")
                writer.write("${record.synced}\n")
            }
        }
        
        return file
    }
    
    fun formatDate(date: Date): String {
        return displayDateFormat.format(date)
    }
    
    fun getImageFile(fileName: String): File {
        return File(context.filesDir, "images/$fileName")
    }
    
    fun getExportDirectory(): File {
        val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val exportDir = File(externalDir, "cattle_exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        return exportDir
    }
}
