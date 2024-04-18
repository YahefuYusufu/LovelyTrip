
package com.lovelytrip.lovelyTrip.data


import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


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
    val rating: Int,
    val image: Bitmap?
)

