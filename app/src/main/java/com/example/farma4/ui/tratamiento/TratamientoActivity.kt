package com.example.farma4.ui.tratamiento

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.R
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityTratamientoBinding


class TratamientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTratamientoBinding
    private lateinit var tratamientoViewModel: TratamientoViewModel
   // private lateinit var adapter: MedicinaViewAdapter
    private lateinit var adapter: TratamientoViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tratamiento)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tratamiento)

        // crear viewModel
        val medicinaDAO = MedicinaDatabase.getInstance(application)!!.medicinaDAO
        val repository = MedicinaRepository(medicinaDAO)
        val factory = TratamientoViewModelFactory(repository)
        tratamientoViewModel =
            ViewModelProvider(this, factory).get(TratamientoViewModel::class.java)

        // vincular xml con viewmodel
        binding.myViewModel = tratamientoViewModel
        binding.lifecycleOwner = this

        //Observar mensajes
        tratamientoViewModel.message.observe(this) { it ->

            it.getContentIfNotHandled()?.let { its ->
                Log.i("MyTAG", "toast ${its}")
                Toast.makeText(this, its, Toast.LENGTH_LONG).show()
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.medicinaRecyclerView.layoutManager = LinearLayoutManager(this)
    //    adapter = MedicinaViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        adapter = TratamientoViewAdapter(this, { selectedItem: Medicina -> listItemClicked(selectedItem) })

        binding.medicinaRecyclerView.adapter = adapter
        displayMedicinasList()
    }

    private fun displayMedicinasList() {
        tratamientoViewModel.getSavedMedicinas().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado listItemClicked")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        tratamientoViewModel.clickMedicina(medicina)

    }
}