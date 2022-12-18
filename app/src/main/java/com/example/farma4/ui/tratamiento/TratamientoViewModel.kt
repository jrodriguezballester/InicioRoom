package com.example.farma4.ui.tratamiento

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.example.farma4.Event
import com.example.farma4.database.model.Medicina
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.tests.Utilidades
import kotlinx.coroutines.launch
import java.util.*

class TratamientoViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private lateinit var medicina: Medicina
    val deletedMedicina = ArrayList<Medicina>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

    val inputName = MutableLiveData<String>()
    val inputPrincipio = MutableLiveData<String>()
    val inputDosis = MutableLiveData<String>()
    val inputUnidadesCaja = MutableLiveData<String>()
    val inputStock = MutableLiveData<String>()
    val inputFechaStock = MutableLiveData<String>()

    val desayunoList: MutableList<Medicina> = mutableListOf<Medicina>()
    val comidaList: MutableList<Medicina> = mutableListOf<Medicina>()
    val cenaList: MutableList<Medicina> = mutableListOf<Medicina>()
    val resoponList: MutableList<Medicina> = mutableListOf<Medicina>()





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

    fun clickMedicina() {
               //eliminarlo de las listas
  //      val tratamientoActivity:TratamientoActivity=TratamientoActivity()
//tratamientoActivity.desayunoMedicinasList2()

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


    fun withMultiChoiceList(medicina: Medicina, context: Context) {
        var cero: String = "0"
        var ceroChar = cero[0]
        var indice = 0
        var items = arrayOf("Desayuno", "Comida", "Cena", "Resopon")

        for (num in medicina.dosis) {
            indice += 1
            if (num == ceroChar) {
                when (indice) {
                    1 -> items[indice - 1] = "-"
                    2 -> items[indice - 1] = "-"
                    3 -> items[indice - 1] = "-"
                    4 -> items[indice - 1] = "-"
                }
            }
        }


        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(context)

        builder.setTitle("This is list choice dialog box")
        builder.setMultiChoiceItems(items, null)
        { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("DONE") { dialogInterface, i ->
            Log.i("Inventario", "pulsado OK -${selectedList}--------")

            ArrayList<Medicina>().add(medicina)

            val selectedStrings = ArrayList<String>()

            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])
            }
            Log.i("Inventario", "pulsado OK -${selectedStrings}--------")

            Toast.makeText(
                context,
                "Items selected are: " + Arrays.toString(selectedStrings.toTypedArray()),
                Toast.LENGTH_LONG
            ).show()
            clickMedicina()
        }
        builder.show()
    }

}