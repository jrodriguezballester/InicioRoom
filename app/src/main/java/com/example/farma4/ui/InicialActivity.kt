package com.example.farma4.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.R
import com.example.farma4.databinding.ActivityInicialBinding

class InicialActivity : AppCompatActivity() {

    private lateinit var inicialViewModel: InicialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)
        Log.i("MenuActivity", "valor")

        inicialViewModel = ViewModelProvider(this).get(InicialViewModel::class.java)
        val binding: ActivityInicialBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_inicial)
// // Bind layout with ViewModel
        binding.myViewModel = inicialViewModel
//
//        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        Log.i("MenuActivity", "valor::${inicialViewModel.cambioActivity.value.toString()}")

        inicialViewModel.cambioActivity.observe(this) {
            Log.i("MenuActivity", "valor: ${it}")
          // medicamentos
            if (it == 1) {
                Log.i("MenuActivity", "entra1: ${it}")
                val intent = Intent(this, AddMedicinaActivity::class.java)
                startActivity(intent)
            }
            // Tratamiento
            if (it == 2) {
                Log.i("MenuActivity", "entra2: ${it}")
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
            // Inventario
            if (it == 3) {
                Log.i("MenuActivity", "entra3: ${it}")
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
        }
    }
}