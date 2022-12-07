package com.example.farma4.ui.inventario

import android.util.Log
import androidx.lifecycle.*
import com.example.farma4.Event
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaRepository
import kotlinx.coroutines.launch

class InventarioViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

    fun addCajas(numCajas: Int, medicina: Medicina) {
        // numero de comprimidos
        val numComprimidosASumar = medicina.unidadesCaja * numCajas
        Log.i(
            "MyTAG",
            "Antes update ${medicina.name},${medicina.principio},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},${medicina.fechaStock},"
        )
        medicina.stock += numComprimidosASumar
        updateMedicina(medicina)
    }

    private fun updateMedicina(medicina: Medicina) {
        Log.i(
            "MyTAG",
            "update ${medicina.name},${medicina.principio},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},${medicina.fechaStock},"
        )
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
