package com.example.farma4.ui.medicamentos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.databinding.ActivityMedicamentosBinding

class MedicamentosActivity : AppCompatActivity() {
    private lateinit var adapter: MedicamentosViewAdapter
    private lateinit var medicinaRepository: MedicinaRepository


    private lateinit var binding: ActivityMedicamentosBinding
    private lateinit var medicamentosViewModel: MedicamentosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicamentos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicamentos)

        val factory = MedicamentosViewModelFactory(MyApp.medicinaRepository!!)
        medicamentosViewModel =
            ViewModelProvider(this, factory).get(MedicamentosViewModel::class.java)

        // vincular xml con viewmodel
        binding.myViewModel = medicamentosViewModel
        binding.lifecycleOwner = this

        //Observar Mensajes
        medicamentosViewModel.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let { its ->
                Log.i("MyTAG", "toast ${its}")
                Toast.makeText(this, its, Toast.LENGTH_LONG).show()
            }
        }

        //Ocultar Formulario
        binding.formularioView.visibility = View.GONE

        //Iniciar RecyclerView
        initRecyclerView()

        //Boton flotante
        binding.floadButton.setOnClickListener { view ->
            medicamentosViewModel.resetForm()
            medicamentosViewModel.clickado=false
            binding.formularioView.visibility = View.VISIBLE
        }

    }

    private fun initRecyclerView() {
        binding.medtosRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MedicamentosViewAdapter { selectedItem: Medicina -> itemClicked(selectedItem) }
        binding.medtosRecyclerView.adapter = adapter
        displayMedicinasList()
    }


    private fun displayMedicinasList() {
        medicamentosViewModel.getSavedMedicinas().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun itemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado itemClicked")

        binding.formularioView.visibility = View.VISIBLE
        medicamentosViewModel.medClicked=medicina
        medicamentosViewModel.clickado=true
        medicamentosViewModel.rellenarFormConDatos(medicina)

    }
}
