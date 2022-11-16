package com.example.farma4

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farma4.database.Medicina
import com.example.farma4.database.MedicinaRepository
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MedicinaViewModel(private val repository: MedicinaRepository): ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        // pasamos el texto del boton al cargar ¿xq no poner directo?

        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!

        insertMedicina(Medicina( name, email, unidadesCaja =1 , stock =1 ))
        inputName.value = ""
        inputEmail.value = ""
    }

    private fun insertMedicina(medicina: Medicina) {
        viewModelScope.launch { repository.insert(medicina) }
    }
}