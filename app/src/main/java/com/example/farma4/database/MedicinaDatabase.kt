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

//
@Database(entities = [Medicina::class], version = 5, exportSchema = false)
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
    }
}

//
//        val dates = "20221127"
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        val fechaStock1 = LocalDate.parse(dates, DateTimeFormatter.BASIC_ISO_DATE)
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        var fechaStock = Date.from(fechaStock1.atStartOfDay(ZoneId.systemDefault()).toInstant())
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        val PREPOPULATE_DATA =
//            listOf<Medicina>(
//                Medicina("Zarelis", "Venlafaxina", "1000", 30, 60, fechaStock),
//                Medicina("Dormodor", "Fluracepam", "0001", 30, 36, fechaStock),
//                Medicina("Mirtazapina", "Mirtazapina", "1001", 30, 64, fechaStock),
//                Medicina("Ziprasidona 60", "-", "0110", 56, 57, fechaStock),
//                Medicina("Ziprexa", "Olanzapina 5", "0102", 28, 87, fechaStock),
//                Medicina("Abilify 15", "Aripiprazol 15", "1000", 28, 53, fechaStock),
//                Medicina("Abilify 5", "Aripiprazol 5", "1000", 28, 45, fechaStock),
//                Medicina("Etumina", "Clotiapina", "1101", 30, 75, fechaStock),
//                Medicina("Tranxilium", "Clorazepato", "5110", 20, 59, fechaStock),
//                Medicina("Noctamid", "Lormetazepam 2", "0001", 20, 41, fechaStock),
//                Medicina("Trankimazin", "Alprozolam", "0002", 30, 42, fechaStock),
//
//                )
//    }

//}