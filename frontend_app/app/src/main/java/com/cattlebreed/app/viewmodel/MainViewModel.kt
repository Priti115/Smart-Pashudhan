package com.cattlebreed.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cattlebreed.app.data.entity.AnimalRecord
import com.cattlebreed.app.data.repository.AnimalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainViewModel(
    private val repository: AnimalRepository
) : ViewModel() {
    
    val allRecords: StateFlow<List<AnimalRecord>> = repository.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    fun saveAnimalRecord(imagePath: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val record = AnimalRecord(
                    animalId = "ANIMAL_${UUID.randomUUID().toString().substring(0, 8).uppercase()}",
                    date = Date(),
                    imagePath = imagePath,
                    bodyLength = 100.0 + (0..50).random(), // Dummy values
                    height = 120.0 + (0..30).random(),
                    chestWidth = 50.0 + (0..20).random(),
                    rumpAngle = 10.0 + (0..15).random(),
                    atcScore = 70 + (0..30).random(),
                    synced = false
                )
                
                repository.insertRecord(record)
                _message.value = "Animal record saved successfully!"
            } catch (e: Exception) {
                _message.value = "Error saving record: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
    
    fun getRecordById(id: Long, callback: (AnimalRecord?) -> Unit) {
        viewModelScope.launch {
            try {
                val record = repository.getRecordById(id)
                callback(record)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }
    
    fun deleteRecord(record: AnimalRecord) {
        viewModelScope.launch {
            try {
                repository.deleteRecord(record)
                _message.value = "Record deleted successfully"
            } catch (e: Exception) {
                _message.value = "Error deleting record: ${e.message}"
            }
        }
    }
}