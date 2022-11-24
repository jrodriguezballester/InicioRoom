package com.example.farma4.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InicialViewModel() : ViewModel() {
    val cambioActivity = MutableLiveData<Int>()
    var buttonToActivity1 = "Añadir Medicina"
    var buttonToActivity2 = "Ver Listado Medicina"

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
    fun toListMedicinas() {
        Log.i("InicialActivity", "pulsado List")
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 2
        Log.i("InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")

    }
}