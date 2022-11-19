// https://appdevnotes.com/android-mvvm-project-example/#event

package com.example.farma4

import android.util.Log
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

    private var isUpdateOrDelete = false
    private lateinit var medicinaToUpdateOrDelete: Medicina

    init {
        alternarTextButtons(true)
    }

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

    fun saveOrUpdate() {
        val name: String
        val dosis: String
        val unidadesCaja: Int
        val stock: Int

        if (validar()) {
            name = inputName.value!!
            dosis = inputDosis.value!!
            unidadesCaja = inputUnidadesCaja.value!!.toInt()
            stock = inputUnidadesCaja.value!!.toInt()
            if (isUpdateOrDelete) {
                medicinaToUpdateOrDelete.name = name
                medicinaToUpdateOrDelete.dosis = dosis
                medicinaToUpdateOrDelete.unidadesCaja = unidadesCaja
                medicinaToUpdateOrDelete.stock = stock

                updateMedicina(medicinaToUpdateOrDelete)

            } else {
                insertMedicina(Medicina(name, dosis, unidadesCaja, stock))
                resetFormulario()
            }
        }
    }

    private fun updateMedicina(medicina: Medicina) {
        Log.i(
            "MedicinaViewModel",
            "update ${medicina.name},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},"
        )
        viewModelScope.launch {
            val rows = repository.update(medicina)
            if (rows > 0) {
                resetFormulario()
                isUpdateOrDelete = false
                alternarTextButtons(true)
                statusMessage.value = Event("$rows Row Updated Successfully")
            } else {
                statusMessage.value = Event("Error to Updated")
            }
        }
    }

    private fun alternarTextButtons(inicio: Boolean) {
        if (inicio) {
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
        } else {
            saveOrUpdateButtonText.value = "Actualizar"
            clearAllOrDeleteButtonText.value = "Borrar"
        }

    }

    private fun resetFormulario() {
        inputName.value = ""
        inputDosis.value = ""
        inputUnidadesCaja.value = ""
        inputStock.value = ""
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
        if(isUpdateOrDelete){
            deleteMedicina(medicinaToUpdateOrDelete)
        }else{
            clearAll()
        }

    }

    private fun deleteMedicina(medicina: Medicina) {
        Log.i(
            "MedicinaViewModel",
            "delete ${medicina.name},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},"
        )
        viewModelScope.launch {
            val rows = repository.delete(medicina)
            if (rows > 0) {
                resetFormulario()
                isUpdateOrDelete = false
                alternarTextButtons(true)
                statusMessage.value = Event("$rows Row Updated Successfully")
            } else {
                statusMessage.value = Event("Error to Updated")
            }
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            val noOfRowsDeleted = repository.deleteAll()
            if (noOfRowsDeleted > 0) {
                statusMessage.value = Event("$noOfRowsDeleted Subscribers Deleted Successfully")
            } else {
                statusMessage.value = Event("Error Occurred")
            }
        }
    }

    fun initUpdateAndDelete(medicina: Medicina) {
        isUpdateOrDelete = true

        alternarTextButtons(false)

        inputName.value = medicina.name
        inputDosis.value = medicina.dosis
        inputStock.value = medicina.stock.toString()
        inputUnidadesCaja.value = medicina.unidadesCaja.toString()

        medicinaToUpdateOrDelete = medicina
    }
}