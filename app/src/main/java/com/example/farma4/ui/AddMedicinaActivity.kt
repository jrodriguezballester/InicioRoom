package com.example.farma4.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MedicinaViewAdapter
import com.example.farma4.MedicinaViewModel
import com.example.farma4.MedicinaViewModelFactory
import com.example.farma4.R
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaDAO
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

class AddMedicinaActivity : AppCompatActivity() {

    private lateinit var medicinaDAO: MedicinaDAO
    private lateinit var medicinaRepo: MedicinaRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var medicinaViewModel: MedicinaViewModel
    private lateinit var adapter: MedicinaViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO
        val medicinaRepository = MedicinaRepository(medicinaDAO)
        val factory = MedicinaViewModelFactory(medicinaRepository)

        medicinaViewModel = ViewModelProvider(this, factory).get(MedicinaViewModel::class.java)
        binding.myViewModel = medicinaViewModel
        binding.lifecycleOwner = this

        medicinaViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        initRecyclerView()

//
//        val db = Room.databaseBuilder(
//            applicationContext,
//            MedicinaDatabase::class.java, "medicina_database"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//
//        medicinaDAO = db.medicinaDAO
//        medicinaRepo= MedicinaRepository(medicinaDAO)
//
//        testDB()

    }

    private fun initRecyclerView() {
        binding.medicinaRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MedicinaViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.medicinaRecyclerView.adapter = adapter
        displayMedicinasList()
    }

    private fun displayMedicinasList() {
        medicinaViewModel.getSavedMedicinas().observe(this,Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MainActivity","pulsado")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        medicinaViewModel.initUpdateAndDelete(medicina)

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
            //       val medicinas = medicinaDAO.getAllMedicinas()
            val medicinas = medicinaRepo.medicinas

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
        val medicinas2: List<Medicina>
        medicinas2 = medicinaDAO.getListMedicinas()
        Log.i("MyTAG", "*****   ${medicinas2.size} medicinas there *****")
        medicinas2.forEach { medicina ->
            Log.i("MyTAG", " name: ${medicina.name} stock: ${medicina.stock}")
        }
    }
}
