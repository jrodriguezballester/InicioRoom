package com.example.farma4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MedicinaViewAdapter
import com.example.farma4.MedicinaViewModel
import com.example.farma4.MedicinaViewModelFactory
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityListaBinding

class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding
    private lateinit var adapter: MedicinaViewAdapter
    private lateinit var medicinaViewModel: MedicinaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO
        val medicinaRepository = MedicinaRepository(medicinaDAO)
        val factory = MedicinaViewModelFactory(medicinaRepository)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista)
        medicinaViewModel = ViewModelProvider(this, factory).get(MedicinaViewModel::class.java)

         initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.medicinaRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MedicinaViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.medicinaRecyclerView.adapter = adapter
        displayMedicinasList()
    }

    private fun displayMedicinasList() {
        medicinaViewModel.getSavedMedicinas().observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MainActivity", "pulsado")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        medicinaViewModel.initUpdateAndDelete(medicina)

    }

}