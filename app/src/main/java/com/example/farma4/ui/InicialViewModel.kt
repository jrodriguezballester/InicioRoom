package com.example.farma4.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InicialViewModel() : ViewModel() {
    val cambioActivity = MutableLiveData<Int>()
    var buttonToActivity1 = "Medicamentos"
    var buttonToActivity2 = "Tratamiento"
    var buttonToActivity3 = "Inventario"
    init {
        Log.i("InicialActivity", "viewmodel")
        cambioActivity.value = 0
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
    }

    fun toAddMedicina() {
        Log.i("InicialActivity", "pulsado")
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 1
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")

    }

    fun toTratamiento() {
        Log.i("InicialActivity", "pulsado toTratamiento")
        cambioActivity.value = 2
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")

    }
    fun toInventario() {
        Log.i("InicialActivity", "pulsado toInventario()")
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 3
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")

    }
}