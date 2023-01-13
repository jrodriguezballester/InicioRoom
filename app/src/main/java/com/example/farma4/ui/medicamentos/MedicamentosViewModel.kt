package com.example.farma4.ui.medicamentos

import android.util.Log
import androidx.lifecycle.*
import com.example.farma4.Utilities.Event
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.database.model.MapperImpl
import com.example.farma4.database.model.MedForm
import com.example.farma4.database.model.Medicina
import com.example.farma4.Utilities.Utilidades
import kotlinx.coroutines.launch
import java.util.*


class MedicamentosViewModel(val repository: MedicinaRepository) : ViewModel() {


    private lateinit var medicina: Medicina
    private lateinit var medForm: MedForm
    lateinit var medClicked: Medicina
    private val _mensaje = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _mensaje

    var numberLabelError = MutableLiveData<Int>()
    private val _mensajeError = MutableLiveData<String>()
    val messageError: LiveData<String> = _mensajeError


    val inputName = MutableLiveData<String>()
    val inputDosis = MutableLiveData<String>()
    val inputUnidadesCaja = MutableLiveData<String>()

    val inputStock = MutableLiveData<String>()
    val inputFechaStock = MutableLiveData<String>()
    val inputPrincipio = MutableLiveData<String>()
    val inputFecIniTtoString = MutableLiveData<String>()
    val inputFecFinTtoString = MutableLiveData<String>()

    var fecIniTtoDate = MutableLiveData<Date>()
    var fecFinTtoDate = MutableLiveData<Date>()
    val fecStockDate = MutableLiveData<Date>()

    lateinit var listadoMedicamentos: List<Medicina>

    var clickado = false

    /**
     * Obtiene la lista de medicinas de la BD
     */
    fun getSavedMedicinas() = liveData {
        repository.medicinasOrderByFecFinTto.collect {
            emit(it)
        }
    }


    fun rellenarFormConDatos(medicina: Medicina) {

        inputName.value = medicina.name
        inputPrincipio.value = medicina.principio
        inputDosis.value = medicina.dosis
        inputUnidadesCaja.value = medicina.unidadesCaja.toString()
        inputStock.value = medicina.stock.toString()
        inputFechaStock.value = Utilidades.dateToStringBarra(medicina.fechaStock)
        inputFecIniTtoString.value = Utilidades.dateToStringBarra((medicina.FecIniTto))
        inputFecFinTtoString.value = Utilidades.dateToStringBarra((medicina.FecFinTto))
    }

    fun resetForm() {
        inputName.value = ""
        inputPrincipio.value = ""
        inputDosis.value = ""
        inputUnidadesCaja.value = ""
        inputFecFinTtoString.value = ""
        inputFecIniTtoString.value = ""
        inputStock.value = ""
        inputFechaStock.value = ""
    }

    fun saveButton() {
        // Comprobar los campos
        if (isAllEnter()) {
            // recoger formulario
            medicina = getValueForm()

            // Guardar en BD
            if (clickado) {
                updateMedicina(medicina)
            } else {
                insertMedicina(medicina)
            }
            clickado = false
        }
    }

    fun deleteButton() {
        Log.i("MyTAG", "delete clickado $clickado,${medClicked.name},")
        if (!inputName.value.isNullOrEmpty()) {
            medicina = if (clickado) {
                medClicked
            } else {
                MapperImpl.MedNameTOMedicina(inputName.value!!)
            }
            deleteMedicina(medicina)
            clickado = false
        }
    }

    /**
     * Crea medForm con los valores del formulario
     * (8 campos)
     */
    private fun getValueForm(): Medicina {

        Log.i("MyTAG", "isAllEnter() ${isAllEnter()}")

        val name: String = inputName.value!!
        val principio: String = inputName.value!!
        val dosis: String = inputDosis.value!!
        val unidadesCaja: Int = inputUnidadesCaja.value!!.toInt()
        val fecIniTto: Date = fecIniTtoDate.value!!
        val fecFinTto: Date = fecFinTtoDate.value!!
        val stock: Int = inputDosis.value!!.toInt()
        val fecStoch: Date = fecStockDate.value!!

        return Medicina(name, principio, dosis, unidadesCaja, stock, fecStoch, fecIniTto, fecFinTto)
    }

    /**
     * Verifica: Los campos del Formulario no pueden ser nulos, UnidadesCaja y Stock deben ser Int,
     * las fechas deben estar separadas por /
     */
    fun isAllEnter(): Boolean {
        Log.i("MyTAG", "... isAllEnter ")
        var validado = false
        // Validando entrada datos
        if (inputName.value.isNullOrEmpty()) {
            _mensaje.value = Event("Please enter name")
            _mensajeError.value="Please enter name"
            numberLabelError.value = 1
        } else if (inputPrincipio.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter principio")
        else if (inputUnidadesCaja.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter Unidades por Caja")
        else if (inputDosis.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter dosis")
        else if (inputFecIniTtoString.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter Fecha Inicio Tto")
        else if (inputFecFinTtoString.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter Fecha Fin Tto")
        else if (inputStock.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter Stock")
        else if (inputFechaStock.value.isNullOrEmpty()) _mensaje.value =
            Event("Please enter FechaStock")
        else validado = true

        // Especificando valor contenido
        // UnidadesCaja :Int
        if (!inputUnidadesCaja.value.isNullOrEmpty()) {
            if (inputUnidadesCaja.value!!.toIntOrNull() == null) {
                _mensaje.value = Event("Please enter el numero de unidades ")
                validado = false
            }
        }

        // Stock :Int
        if (!inputStock.value.isNullOrEmpty()) {
            if (inputStock.value!!.toIntOrNull() == null) {
                _mensaje.value = Event("Please enter el numero de Stock ")
                validado = false
            }
        }


        // Fechas formato
        try {
            fecIniTtoDate.value = Utilidades.stringBarraToDate(inputFecIniTtoString.value!!)
            fecFinTtoDate.value = Utilidades.stringBarraToDate(inputFecFinTtoString.value!!)
            fecStockDate.value = Utilidades.stringBarraToDate(inputFechaStock.value!!)
            Log.i("MyTAG", "... isAllEnter prueba ${fecIniTtoDate.value} ")
            Log.i("MyTAG", "... isAllEnter prueba ${fecFinTtoDate.value} ")
            Log.i("MyTAG", "... isAllEnter prueba ${fecStockDate.value} ")
        } catch (e: java.lang.Exception) {
            _mensaje.value = Event("Please formato fecha incorrecto ")
            validado = false
        }

        return validado
    }
//    fun validate(view: View?) {
//        var mailError: String? = null
//        if (TextUtils.isEmpty(editTextEmail.getText())) {
//            mailError = getString(R.string.mandatory)
//        }
//        toggleTextInputLayoutError(textInputEmail, mailError)
//        var passError: String? = null
//        if (TextUtils.isEmpty(editTextPassword.getText())) {
//            passError = getString(R.string.mandatory)
//        }
//        toggleTextInputLayoutError(textInputPassword, passError)
//        clearFocus()
//    }
//
//    /**
//     * Display/hides TextInputLayout error.
//     *
//     * @param msg the message, or null to hide
//     */
//    private fun toggleTextInputLayoutError(
//        textInputLayout: TextInputLayout,
//        msg: String?
//    ) {
//        textInputLayout.error = msg
//        textInputLayout.isErrorEnabled = msg != null
//    }


    private fun insertMedicina(medicina: Medicina) {
        viewModelScope.launch {
            val newRowId = repository.insert(medicina)
            if (newRowId > -1) {
                _mensaje.value = Event("Subscriber Inserted Successfully $newRowId")
                resetForm()
            } else {
                _mensaje.value = Event("Error Occurred")
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
                resetForm()
                _mensaje.value = Event("$rows Row Updated Successfully")
            } else {
                _mensaje.value = Event("Error to Updated")
            }
        }
    }

    private fun deleteMedicina(medicina: Medicina) {
        Log.i("MyTAG", "delete ${medicina.name},${medicina.dosis},")
        viewModelScope.launch {
            val rows = repository.delete(medicina)
            if (rows > 0) {
                resetForm()
                _mensaje.value = Event("$rows Row Delete Successfully")
            } else {
                _mensaje.value = Event("Error to delete")
            }
        }
    }
}