package com.example.farma4.ui.prescripcion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.ui.medicamentos.MedicamentosViewModel
import java.lang.IllegalArgumentException

class PrescripcionViewModelFactory(private val repository: MedicinaRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrescripcionViewModel::class.java)) {
            return PrescripcionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }



}
