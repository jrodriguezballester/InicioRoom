package com.example.farma4.ui.inicial

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.farma4.database.MedicinaDatabase
import kotlinx.coroutines.flow.onCompletion

class InicialViewModel() : ViewModel() {
    val cambioActivity = MutableLiveData<Int>()
    var buttonToActivity1 = "Medicamentos"
    var buttonToActivity2 = "Caducidad Recetas"
    var buttonToActivity3 = "Inventario"
    var buttonToActivity4 = "Tratamiento"

    init {
        Log.i("MyTAG InicialActivity", "init viewmodel")
        cambioActivity.value = 0
        Log.i("MyTAG InicialActivity ", "valor::${cambioActivity.value.toString()}")

    }

    fun toAddMedicina() {
        Log.i("MyTAG InicialActivity", "pulsado toAddMedicina")
        Log.i("MyTAG toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 1
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
    }

    fun toTratamiento() {
        Log.i("MyTAG InicialActivity", "pulsado toTratamiento")
        cambioActivity.value = 2
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")

    }

    fun toInventario() {
        Log.i("MyTAG InicialActivity", "pulsado toInventario()")
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 3
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
    }

    fun toAddStock() {
        Log.i("MyTAG InicialActivity", "pulsado toAddStock()")
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
        cambioActivity.value = 4
        Log.i("MyTAG InicialActivity toAddMedicina()", "valor::${cambioActivity.value.toString()}")
    }

    fun iniciarBD(context: Context) {
        val medicinaDAO = MedicinaDatabase.getInstance(context).medicinaDAO
        val myFlow = medicinaDAO.getAllMedicinas()
        myFlow.onCompletion {
            Log.i("MyTAG MenuActivity", "myFlow::${myFlow}")

        }
        Log.i("MyTAG MenuActivity", "fin::${medicinaDAO.getAllMedicinas()}")

    }
}