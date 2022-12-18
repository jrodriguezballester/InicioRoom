package com.example.farma4.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MedicinaViewAdapter
import com.example.farma4.MedicinaViewModel
import com.example.farma4.MedicinaViewModelFactory
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaDAO
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityMainBinding

class AddMedicinaActivity : AppCompatActivity() {

    private lateinit var medicinaDAO: MedicinaDAO
    private lateinit var medicinaRepository: MedicinaRepository
    private lateinit var binding: ActivityMainBinding
    private lateinit var medicinaViewModel: MedicinaViewModel
    private lateinit var adapter: MedicinaViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO
        Log.i("MyTAG", ": medicinaDAO::${medicinaDAO}")
        medicinaRepository = MedicinaRepository(medicinaDAO)
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
    }

    private fun initRecyclerView() {
        binding.medicinaRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MedicinaViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.medicinaRecyclerView.adapter = adapter
        displayMedicinasList()
    }

    private fun displayMedicinasList() {
        medicinaViewModel.getSavedMedicinas().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado listItemClicked")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        medicinaViewModel.initUpdateAndDelete(medicina)

    }
}
