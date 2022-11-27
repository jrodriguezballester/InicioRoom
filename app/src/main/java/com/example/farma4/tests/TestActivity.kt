package com.example.farma4.tests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.farma4.MedicinaViewModelFactory
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaDAO
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*

class TestActivity : AppCompatActivity() {
    private lateinit var medicinaDAO: MedicinaDAO
    private lateinit var medicinaRepo: MedicinaRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //     val medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO

        val db = Room.databaseBuilder(
            applicationContext,
            MedicinaDatabase::class.java, "medicina_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        medicinaDAO = db.medicinaDAO
        medicinaRepo = MedicinaRepository(medicinaDAO)
        val medicinaRepository = MedicinaRepository(medicinaDAO)
        val factory = MedicinaViewModelFactory(medicinaRepository)

        testDB()

    }

    private fun testDB() {
        val fechaStock = Date() //todo puesto para poder continuar
        lifecycleScope.launch(Dispatchers.IO) {
            //Insert
            Log.i("MyTAG", "*****     Inserting 3 medicinas     **********")
            medicinaDAO.insertMedicina(Medicina("qq", 1, 1, 30, fechaStock))
            medicinaDAO.insertMedicina(Medicina("zz", 1, 1, 30, fechaStock))
            medicinaDAO.insertMedicina(Medicina("aa", 1, 1, 30, fechaStock))
            Log.i("MyTAG", "*****     Inserted 3 medicinas       **********")

            //Query
            //       val medicinas = medicinaDAO.getAllMedicinas()
            val medicinas = medicinaRepo.medicinas

            Log.i("MyTAG", "*****    medicinas $medicinas  *****")


            ////// mostrar el listado ////
            mostrarListadoFlow1(medicinas)

            // Ver resultado
            mostrarListado()

            //Update
            Log.i("MyTAG", "*****      Updating a medicina      **********")
            medicinaDAO.updateMedicina(Medicina("aa", 1, 1, 3000, fechaStock))

            // Ver resultado
            mostrarListado()

            //delete
            Log.i("MyTAG", "*****       Deleting a medicina      **********")
            medicinaDAO.deleteMedicina(Medicina("qq", 1, 1, 30, fechaStock))

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
        val medicinas2: List<Medicina>
        medicinas2 = medicinaDAO.getListMedicinas()
        Log.i("MyTAG", "*****   ${medicinas2.size} medicinas there *****")
        medicinas2.forEach { medicina ->
            Log.i("MyTAG", " name: ${medicina.name} stock: ${medicina.stock}")
        }
    }
}

