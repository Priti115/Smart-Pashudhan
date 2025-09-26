package com.cattlebreed.app.data.repository

import com.cattlebreed.app.data.dao.AnimalRecordDao
import com.cattlebreed.app.data.entity.AnimalRecord
import kotlinx.coroutines.flow.Flow

class AnimalRepository(
    private val animalRecordDao: AnimalRecordDao
) {
    
    fun getAllRecords(): Flow<List<AnimalRecord>> = animalRecordDao.getAllRecords()
    
    suspend fun getRecordById(id: Long): AnimalRecord? = animalRecordDao.getRecordById(id)
    
    suspend fun insertRecord(record: AnimalRecord): Long = animalRecordDao.insertRecord(record)
    
    suspend fun updateRecord(record: AnimalRecord) = animalRecordDao.updateRecord(record)
    
    suspend fun deleteRecord(record: AnimalRecord) = animalRecordDao.deleteRecord(record)
    
    suspend fun deleteAllRecords() = animalRecordDao.deleteAllRecords()
    
    suspend fun getUnsyncedRecords(): List<AnimalRecord> = animalRecordDao.getUnsyncedRecords()
    
    suspend fun markAsSynced(id: Long) = animalRecordDao.markAsSynced(id)
}