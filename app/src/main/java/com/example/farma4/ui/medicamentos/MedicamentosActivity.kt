package com.example.farma4.ui.medicamentos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.BaseActivity
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ActivityMedicamentosBinding

class MedicamentosActivity : BaseActivity() {
    private lateinit var adapter: MedicamentosViewAdapter
    private lateinit var binding: ActivityMedicamentosBinding
    private lateinit var medicamentosViewModel: MedicamentosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicamentos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicamentos)

        val factory = MedicamentosViewModelFactory(MyApp.medicinaRepository!!)
        medicamentosViewModel =
            ViewModelProvider(this, factory)[MedicamentosViewModel::class.java]

        // vincular xml con viewmodel
        binding.myViewModel = medicamentosViewModel
        binding.lifecycleOwner = this

        //Observar Mensajes
        medicamentosViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let { its ->
                Log.i("MyTAG", "toast $its")
                Toast.makeText(this, its, Toast.LENGTH_LONG).show()
            }
        }

        medicamentosViewModel.numberLabelError.observe(this) {
            when (it) {
                1 -> binding.textInputName.error = medicamentosViewModel.messageError.value

            }
        }

        //Ocultar Formulario
        binding.formularioView.visibility = View.GONE

        medicamentosViewModel.formularioVisible.observe(this) {
            Log.i("MyTAG", "close clickado valor:$it")

                if (it) {
                    binding.formularioView.visibility = View.VISIBLE // Si es true, muestra el LinearLayout
                } else {
                    binding.formularioView.visibility = View.GONE // Si es false, oculta el LinearLayout
                }

        }


        //Iniciar RecyclerView
        initRecyclerView()

        //Boton flotante
        binding.floadButton.setOnClickListener {
            medicamentosViewModel.resetForm()
            medicamentosViewModel.clickado = false
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
        Log.i("MyTAG", "displayMedicinasList()")
        medicamentosViewModel.getSavedMedicinas().observe(this) {
            Log.i("MyTAG", "displayMedicinasList() $it")
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun itemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado itemClicked")

      //  binding.formularioView.visibility = View.VISIBLE
        medicamentosViewModel.cambiarVisibilidadLinearLayout(true)
        medicamentosViewModel.medClicked = medicina
        medicamentosViewModel.clickado = true
        medicamentosViewModel.rellenarFormConDatos(medicina)

    }
}
