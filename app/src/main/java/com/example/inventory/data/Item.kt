
package com.example.inventory.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.Date

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val country: String,
    val city: String,
    val summary: String,
    val addedDate: Long,
    val rating: Int
)

