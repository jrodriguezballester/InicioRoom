package com.example.farma4.ui.inventario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.R
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityInventarioBinding
import com.example.farma4.ui.tratamiento.TratamientoViewAdapter

class InventarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventarioBinding
    private lateinit var adapter: InventarioViewAdapter
    private lateinit var inventarioViewModel: InventarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)
        Log.i("Inventario", "XXXXXX")



        val medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO
        val medicinaRepository = MedicinaRepository(medicinaDAO)
        val factory = InventarioViewModelFactory(medicinaRepository)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventario)
        inventarioViewModel = ViewModelProvider(this, factory).get(InventarioViewModel::class.java)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.inventarioRecyclerView.layoutManager=LinearLayoutManager(this)
        adapter= InventarioViewAdapter(this,{ selectedItem: Medicina -> listItemClicked(selectedItem) })
        binding.inventarioRecyclerView.adapter=adapter
        displayMedicinasList()
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("Inventario", "pulsado")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
    }

    private fun displayMedicinasList() {
        inventarioViewModel.getSavedMedicinas().observe(this){
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }
}