package com.example.farma4.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.R
import com.example.farma4.databinding.ActivityInicialBinding
import com.example.farma4.ui.inventario.InventarioActivity
import com.example.farma4.ui.medicamentos.MedicamentosActivity
import com.example.farma4.ui.prescripcion.PrescripcionActivity
import com.example.farma4.ui.tratamiento.TratamientoActivity

class InicialActivity : AppCompatActivity() {

    private lateinit var inicialViewModel: InicialViewModel
    private lateinit var binding: ActivityInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        inicialViewModel = ViewModelProvider(this).get(InicialViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inicial)
        // Bind layout with ViewModel (enlaza con el xml)
        binding.myViewModel = inicialViewModel
        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this
     //   inicialViewModel.iniciarBD(this)
        Log.i("MyTAG", "InicialActivity valor::${inicialViewModel.cambioActivity.value.toString()}")

        inicialViewModel.cambioActivity.observe(this) {
            Log.i("MyTAG", "valor: ${it}")
            when (it) {
                // medicamentos
                1 -> startActivity<MedicamentosActivity>()
                // Tratamiento
                2 -> startActivity<TratamientoActivity>()
                // Inventario
                3 -> startActivity<InventarioActivity>()
                // Inventario
                4 -> startActivity<PrescripcionActivity>()
            }
        }
    }

    inline fun <reified T : Activity> Context.createIntent() =
        Intent(this, T::class.java)

    inline fun <reified T : Activity> Activity.startActivity() {
        startActivity(createIntent<T>())
    }


}