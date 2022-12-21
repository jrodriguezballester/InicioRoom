package com.example.farma4.ui.prescripcion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.databinding.ActivityPrescripcionBinding
import com.example.farma4.ui.medicamentos.MedicamentosViewModel

class PrescripcionActivity : AppCompatActivity() {

    private lateinit var adapter: PrescripcionViewAdapter
    private lateinit var binding: ActivityPrescripcionBinding
    private lateinit var prescripcionViewModel: PrescripcionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescripcion)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescripcion)
        val factory = PrescripcionViewModelFactory(MyApp.medicinaRepository!!)
        prescripcionViewModel =
            ViewModelProvider(this, factory).get(PrescripcionViewModel::class.java)

        // vincular xml con viewmodel
        binding.myViewModel = prescripcionViewModel
        binding.lifecycleOwner = this

        //Iniciar RecyclerView
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.prescripcionRecyclerView.layoutManager=LinearLayoutManager(this)
        adapter=PrescripcionViewAdapter()
        binding.prescripcionRecyclerView.adapter=adapter
        displayMedicinasList()

    }
    private fun displayMedicinasList() {
        prescripcionViewModel.getSavedMedicinas().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }
}