package com.example.farma4.ui.tratamiento

import androidx.lifecycle.*
import com.example.farma4.Utilities.Event
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.database.model.Medicina
import kotlinx.coroutines.launch

class TratamientoViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    fun actualizarMedicina(medicina: Medicina) {
        viewModelScope.launch {
            val rows = repository.update(medicina)
            if (rows > 0) {
                statusMessage.value = Event("$rows Row Updated Successfully")
            } else {
                statusMessage.value = Event("Error to Updated")
            }
        }
    }

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

}