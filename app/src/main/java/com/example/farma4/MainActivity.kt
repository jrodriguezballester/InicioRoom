package com.example.farma4

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaDAO
import com.example.farma4.database.MedicinaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

class MainActivity : AppCompatActivity() {
    private lateinit var medicinaDAO: MedicinaDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            MedicinaDatabase::class.java, "medicina_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        medicinaDAO = db.medicinaDAO
        testDB()

    }

    private fun testDB() {

        lifecycleScope.launch(Dispatchers.IO) {
            //Insert
            Log.i("MyTAG", "*****     Inserting 3 medicinas     **********")
            medicinaDAO.insertMedicina(Medicina("qq", "1", 1, 30))
            medicinaDAO.insertMedicina(Medicina("zz", "1", 1, 30))
            medicinaDAO.insertMedicina(Medicina("aa", "1", 1, 30))
            Log.i("MyTAG", "*****     Inserted 3 medicinas       **********")

            //Query
            val medicinas = medicinaDAO.getAllMedicinas()
            Log.i("MyTAG", "*****    medicinas $medicinas  *****")


            ////// mostrar el listado ////
            mostrarListadoFlow1(medicinas)

            // Ver resultado
            mostrarListado()

            //Update
            Log.i("MyTAG", "*****      Updating a medicina      **********")
            medicinaDAO.updateMedicina(Medicina("aa", "1", 1, 300000))

            // Ver resultado
            mostrarListado()

            //delete
            Log.i("MyTAG", "*****       Deleting a medicina      **********")
            medicinaDAO.deleteMedicina(Medicina("qq", "1", 1, 30))

            // Ver resultado
            mostrarListado()
        }
    }

    private fun mostrarListadoFlow1(medicinas: Flow<List<Medicina>>) {
        runBlocking<Unit> {
            Log.i("MyTAG", "*****    medicinas VVV  *****")
            withTimeoutOrNull(250) { // Timeout after 250ms
                medicinas.collect { value -> println(value) }
            }
            Log.i("MyTAG", "*****    medicinas ^^^^  *****")
        }
    }

    private fun mostrarListado() {
        //Query
        var medicinas2: List<Medicina>
        medicinas2 = medicinaDAO.getListMedicinas()
        Log.i("MyTAG", "*****   ${medicinas2.size} medicinas there *****")
        medicinas2.forEach { medicina ->
            Log.i("MyTAG", " name: ${medicina.name} stock: ${medicina.stock}")
        }
    }
}
