package com.cattlebreed.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "animal_records")
data class AnimalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val animalId: String,
    val date: Date,
    val imagePath: String,
    val bodyLength: Double,
    val height: Double,
    val chestWidth: Double,
    val rumpAngle: Double,
    val atcScore: Int,
    val synced: Boolean = false
)