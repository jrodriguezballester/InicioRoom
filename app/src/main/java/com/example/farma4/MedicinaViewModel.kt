// https://appdevnotes.com/android-mvvm-project-example/#event

package com.example.farma4

import androidx.lifecycle.*
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaRepository
import kotlinx.coroutines.launch

class MedicinaViewModel(private val repository: MedicinaRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputDosis = MutableLiveData<String>()
    val inputUnidadesCaja = MutableLiveData<String>()
    val inputStock = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        // pasamos el texto del boton al cargar ¿xq no poner directo?
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

    fun saveOrUpdate() {

        if (validar()) {
            val name = inputName.value!!
            val dosis = inputDosis.value!!
            val unidadesCaja = inputUnidadesCaja.value!!.toInt()!!
            val stock = inputUnidadesCaja.value!!.toInt()

            insertMedicina(Medicina(name, dosis, unidadesCaja, stock))

            inputName.value = ""
            inputDosis.value = ""
            inputUnidadesCaja.value = ""
            inputStock.value = ""

        }

    }

    private fun validar(): Boolean {
        var validado = false
        if (inputName.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter medicina name")
        } else if (inputDosis.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter dosis")
        } else if (inputUnidadesCaja.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Unidades por Caja")
        } else if (inputStock.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Stock")
        } else {
            validado = true
        }
        return validado
    }

    private fun insertMedicina(medicina: Medicina) {
        viewModelScope.launch {
            val newRowId = repository.insert(medicina)
            if (newRowId > -1) {
                statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
            } else {
                statusMessage.value = Event("Error Occurred")
            }
        }
    }

    fun clearAllOrDelete() {
        clearAll()
    }
    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Subscribers Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}