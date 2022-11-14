package com.example.farma4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Medicina::class], version = 2, exportSchema = false)
abstract class MedicinaDatabase : RoomDatabase() {
    abstract val medicinaDAO: MedicinaDAO

    companion object {
        @Volatile
        private var INSTANCE: MedicinaDatabase? = null
        fun getInstance(context: Context): MedicinaDatabase? {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        MedicinaDatabase::class.java,
                        "medicina_data_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE
            }
        }
    }
}
