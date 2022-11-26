package com.example.farma4.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


@Database(entities = [Medicina::class], version = 2, exportSchema = false)
abstract class MedicinaDatabase : RoomDatabase() {
    abstract val medicinaDAO: MedicinaDAO

    companion object {
        @Volatile
        private var INSTANCE: MedicinaDatabase? = null


        fun getInstance(context: Context): MedicinaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): MedicinaDatabase {
            val actualtDBPath = context.getDatabasePath("medicina_data_database.db").absolutePath
            Log.i("MyTAG ", "MedicinaDatabase buildDatabase 1 :::::::${actualtDBPath}")
            return Room.databaseBuilder(
                context.applicationContext,
                MedicinaDatabase::class.java, "medicina_data_database"
            )
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i("MyTAG ", "MedicinaDatabase buildDatabase 2 llenando BD")

                        GlobalScope.launch(Dispatchers.Main) {
                            getInstance(context).medicinaDAO.insertMedicina(PREPOPULATE_DATA)
                        }
                    }
                })
                .build()
        }

        val PREPOPULATE_DATA = Medicina("XXX", "2", 30, 12)
    }
}
