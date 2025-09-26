package com.cattlebreed.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlebreed.app.data.entity.AnimalRecord
import com.cattlebreed.app.data.repository.AnimalRepository
import com.cattlebreed.app.utils.FileUtils
import com.cattlebreed.app.utils.PDFUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ExportViewModel(
    private val repository: AnimalRepository,
    private val fileUtils: FileUtils,
    private val pdfUtils: PDFUtils
) : ViewModel() {
    
    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()
    
    private val _exportMessage = MutableStateFlow<String?>(null)
    val exportMessage: StateFlow<String?> = _exportMessage.asStateFlow()
    
    private val _exportedFiles = MutableStateFlow<List<File>>(emptyList())
    val exportedFiles: StateFlow<List<File>> = _exportedFiles.asStateFlow()
    
    fun exportToJson() {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                val records = repository.getAllRecords().first()
                val file = fileUtils.exportToJson(records)
                _exportedFiles.value = _exportedFiles.value + file
                _exportMessage.value = "Data exported to JSON successfully! File saved at: ${file.absolutePath}"
            } catch (e: Exception) {
                _exportMessage.value = "Error exporting to JSON: ${e.message}"
            } finally {
                _isExporting.value = false
            }
        }
    }
    
    fun exportToCsv() {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                val records = repository.getAllRecords().first()
                val file = fileUtils.exportToCsv(records)
                _exportedFiles.value = _exportedFiles.value + file
                _exportMessage.value = "Data exported to CSV successfully! File saved at: ${file.absolutePath}"
            } catch (e: Exception) {
                _exportMessage.value = "Error exporting to CSV: ${e.message}"
            } finally {
                _isExporting.value = false
            }
        }
    }
    
    fun exportToPdf() {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                val records = repository.getAllRecords().first()
                val outputDir = fileUtils.getExportDirectory()
                val file = pdfUtils.exportToPDF(records, outputDir)
                _exportedFiles.value = _exportedFiles.value + file
                _exportMessage.value = "Data exported to PDF successfully! Bilingual report saved at: ${file.absolutePath}"
            } catch (e: Exception) {
                _exportMessage.value = "Error exporting to PDF: ${e.message}"
            } finally {
                _isExporting.value = false
            }
        }
    }
    
    fun clearMessage() {
        _exportMessage.value = null
    }
    
    fun clearExportedFiles() {
        _exportedFiles.value = emptyList()
    }
}