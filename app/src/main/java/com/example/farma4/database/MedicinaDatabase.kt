package com.example.farma4.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries.localDate
import java.util.*


@Database(entities = [Medicina::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
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
            return Room.databaseBuilder(
                context.applicationContext,
                MedicinaDatabase::class.java, "medicina_data_database"
            ).fallbackToDestructiveMigration()
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i("MyTAG ", "MedicinaDatabase addCallback llenando BD")
                        GlobalScope.launch(Dispatchers.Main) {
                            PREPOPULATE_DATA.forEach { medicina ->
                                getInstance(context).medicinaDAO.insertMedicina(medicina)
                                Log.i("MyTAG ", "Medicina: ${medicina}")
                            }
                        }
                    }
                })
                .build()
        }


        val dates = "20221120"

        @RequiresApi(Build.VERSION_CODES.O)
        val fechaStock1 = LocalDate.parse(dates, DateTimeFormatter.BASIC_ISO_DATE)
        @RequiresApi(Build.VERSION_CODES.O)
        var fechaStock = Date.from(fechaStock1.atStartOfDay(ZoneId.systemDefault()).toInstant())

        @RequiresApi(Build.VERSION_CODES.O)
        val PREPOPULATE_DATA =
            listOf<Medicina>(
                Medicina("Transilium", 2, 30, 12, fechaStock),
                Medicina("Tranquimacin", 2, 12, 3, fechaStock)

            )
    }
}
