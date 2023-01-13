package com.example.farma4.ui.inventario

import android.util.Log
import androidx.lifecycle.*
import com.example.farma4.Utilities.Event
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.Utilities.Utilidades.Companion.calcularStock
import kotlinx.coroutines.launch
import java.util.Date

class InventarioViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

    fun addCajas(numCajas: Int, medicina: Medicina) {
        Log.i(
            "MyTAG",
            "Antes update ${medicina.name},${medicina.principio},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},${medicina.fechaStock},"
        )
        val numComprimidosASumar = medicina.unidadesCaja * numCajas
        val stockActual= calcularStock(medicina)

        medicina.stock = stockActual+numComprimidosASumar
        medicina.fechaStock=Date()

        updateMedicina(medicina)
    }

    private fun updateMedicina(medicina: Medicina) {
        viewModelScope.launch {
            val rows = repository.update(medicina)
            if (rows > 0) {
                statusMessage.value = Event("$rows Row Updated Successfully")
            } else {
                statusMessage.value = Event("Error to Updated")
            }
        }
    }

}
