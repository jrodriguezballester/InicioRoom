// https://appdevnotes.com/android-mvvm-project-example/#event

package com.example.farma4

import android.util.Log
import androidx.lifecycle.*
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MedicinaViewModel(private val repository: MedicinaRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputDosis = MutableLiveData<String>()
    val inputUnidadesCaja = MutableLiveData<String>()
    val inputStock = MutableLiveData<String>()
    val inputFechaStock = MutableLiveData<String>()

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
        val principio:String
        val dosis: String
        val unidadesCaja: Int
        val stock: Int
        val fechaStockString: String
        val fechaStockDate:Date

        //      val fechaStock= Date() //todo puesto para poder continuar
        if (validar()) {
            name = inputName.value!!
            principio = inputName.value!! //todo
            dosis = inputDosis.value!!
            unidadesCaja = inputUnidadesCaja.value!!.toInt()
            stock = inputUnidadesCaja.value!!.toInt()
            fechaStockString=inputFechaStock.value!!
            fechaStockDate = stringToDate(fechaStockString)

            if (isUpdateOrDelete) {
                medicinaToUpdateOrDelete.name = name
                medicinaToUpdateOrDelete.principio = principio
                medicinaToUpdateOrDelete.dosis = dosis.toString()
                medicinaToUpdateOrDelete.unidadesCaja = unidadesCaja
                medicinaToUpdateOrDelete.stock = stock
                medicinaToUpdateOrDelete.fechaStock = fechaStockDate
                updateMedicina(medicinaToUpdateOrDelete)

            } else {
                insertMedicina(Medicina(name,principio, dosis, unidadesCaja, stock, fechaStockDate))
                resetFormulario()
            }
        }
    }

    private fun stringToDate(fechaStockString: String) =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaStockString) as Date

    private fun dateToString(fechaStock: Date) =
        SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)

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

    private fun validar(): Boolean { //todo falta validar correctamente
        var validado = false
        if (inputName.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter medicina name")
        } else if (inputDosis.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter dosis")
        } else if (inputUnidadesCaja.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Unidades por Caja")
        } else if (inputStock.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Stock")
        } else if (inputFechaStock.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter FechaStock")
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
        if (isUpdateOrDelete) {
            deleteMedicina(medicinaToUpdateOrDelete)
        } else {
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
        inputDosis.value = medicina.dosis.toString()
        inputStock.value = medicina.stock.toString()
        inputUnidadesCaja.value = medicina.unidadesCaja.toString()

        medicinaToUpdateOrDelete = medicina
    }
}