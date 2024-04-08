

package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inventory.components.Converters

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Item::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LovelyTripDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: LovelyTripDatabase? = null

        fun getDatabase(context: Context): LovelyTripDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LovelyTripDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
