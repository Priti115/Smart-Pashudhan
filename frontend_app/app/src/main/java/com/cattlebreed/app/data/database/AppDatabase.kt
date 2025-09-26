package com.cattlebreed.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.cattlebreed.app.data.converter.Converters
import com.cattlebreed.app.data.dao.AnimalRecordDao
import com.cattlebreed.app.data.entity.AnimalRecord

@Database(
    entities = [AnimalRecord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun animalRecordDao(): AnimalRecordDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cattle_breed_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}