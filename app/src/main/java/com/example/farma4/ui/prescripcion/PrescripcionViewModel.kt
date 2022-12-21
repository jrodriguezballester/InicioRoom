package com.example.farma4.ui.prescripcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.farma4.database.MedicinaRepository


class PrescripcionViewModel(val repository: MedicinaRepository):ViewModel() {

    /**
     * Obtiene la lista de medicinas de la BD
     */
    fun getSavedMedicinas() = liveData {
        repository.medicinasOrderByFecFinTto.collect {
            emit(it)
        }
    }
}
