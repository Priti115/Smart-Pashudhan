package com.cattlebreed.app.data.dao

import androidx.room.*
import com.cattlebreed.app.data.entity.AnimalRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalRecordDao {
    
    @Query("SELECT * FROM animal_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<AnimalRecord>>
    
    @Query("SELECT * FROM animal_records WHERE id = :id")
    suspend fun getRecordById(id: Long): AnimalRecord?
    
    @Insert
    suspend fun insertRecord(record: AnimalRecord): Long
    
    @Update
    suspend fun updateRecord(record: AnimalRecord)
    
    @Delete
    suspend fun deleteRecord(record: AnimalRecord)
    
    @Query("DELETE FROM animal_records")
    suspend fun deleteAllRecords()
    
    @Query("SELECT * FROM animal_records WHERE synced = 0")
    suspend fun getUnsyncedRecords(): List<AnimalRecord>
    
    @Query("UPDATE animal_records SET synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long)
}