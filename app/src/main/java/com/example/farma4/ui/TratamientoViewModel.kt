package com.example.farma4.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.farma4.Event
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.tests.Utilidades
import kotlinx.coroutines.launch
import java.util.*

class TratamientoViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private lateinit var medicina: Medicina
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    val inputName = MutableLiveData<String>()
    val inputPrincipio = MutableLiveData<String>()
    val inputDosis = MutableLiveData<String>()
    val inputUnidadesCaja = MutableLiveData<String>()
    val inputStock = MutableLiveData<String>()
    val inputFechaStock = MutableLiveData<String>()

    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

    fun save_button() {
        Log.i("MyTAG", "save ")
        if (validar()) {
            asociarFormulario()
            Log.i("MyTAG", "Validado ${medicina} ")
            insertMedicina(medicina)
        }
    }

    fun update_button() {
        Log.i("MyTAG", "update ")
        if (validar()) {
            asociarFormulario()
            Log.i("MyTAG", "Validado ${medicina} ")
            updateMedicina(medicina)
        }
    }

    fun delete_button() {
        if (validar()) {
            asociarFormulario()
            deleteMedicina(medicina)
        }
    }

    private fun updateMedicina(medicina: Medicina) {
        Log.i(
            "MyTAG",
            "update ${medicina.name},${medicina.principio},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},${medicina.fechaStock},"
        )
        viewModelScope.launch {
            val rows = repository.update(medicina)
            if (rows > 0) {
                resetFormulario()
                statusMessage.value = Event("$rows Row Updated Successfully")
            } else {
                statusMessage.value = Event("Error to Updated")
            }
        }
    }

    private fun insertMedicina(medicina: Medicina) {
        //TODO verificar si ya existe y que hacer si eso, ahora actualiza
        viewModelScope.launch {
            val newRowId = repository.insert(medicina)
            if (newRowId > -1) {
                statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
                resetFormulario()
            } else {
                statusMessage.value = Event("Error Occurred")
                updateMedicina(medicina)
            }
        }
    }

    private fun deleteMedicina(medicina: Medicina) {
        Log.i(
            "MyTAG",
            "delete ${medicina.name},${medicina.dosis},${medicina.unidadesCaja},${medicina.stock},"
        )
        viewModelScope.launch {
            val rows = repository.delete(medicina)
            if (rows > 0) {
                resetFormulario()
                statusMessage.value = Event("$rows Row Delete Successfully")
            } else {
                statusMessage.value = Event("Error to Delete")
            }
        }
    }

    fun clickMedicina(medicina: Medicina) {

        inputName.value = medicina.name
        inputPrincipio.value = medicina.principio
        inputDosis.value = medicina.dosis
        inputStock.value = medicina.stock.toString()
        inputUnidadesCaja.value = medicina.unidadesCaja.toString()
        // transformar fecha
        val fechaStockString: String = Utilidades.dateToString(medicina.fechaStock)
        inputFechaStock.value = fechaStockString

        this.medicina = medicina
    }

    private fun resetFormulario() {
        inputName.value = ""
        inputPrincipio.value = ""
        inputDosis.value = ""
        inputUnidadesCaja.value = ""
        inputStock.value = ""
        inputFechaStock.value = ""

    }

    private fun validar(): Boolean { //todo falta validar correctamente
        var validado = false
        if (inputName.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter medicina name")
        } else if (inputPrincipio.value.isNullOrEmpty()) {
            inputPrincipio.value = "---"
        } else if (inputDosis.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter dosis")
        } else if (inputUnidadesCaja.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Unidades por Caja")
        } else if (inputStock.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Stock")
        } else if (inputFechaStock.value.isNullOrEmpty()) {
            inputFechaStock.value = Utilidades.dateToString(Date())
        } else {
            validado = true
        }
        return validado
    }

    private fun asociarFormulario() {
        // transformar fecha
        val fechaStockString: String = inputFechaStock.value!!
        val fechaStockDate = Utilidades.stringToDate(fechaStockString)

        medicina.name = inputName.value!!
        medicina.principio = inputPrincipio.value!!
        medicina.dosis = inputDosis.value!!
        medicina.unidadesCaja = inputUnidadesCaja.value!!.toInt()
        medicina.stock = inputStock.value!!.toInt()
        medicina.fechaStock = fechaStockDate
    }
}