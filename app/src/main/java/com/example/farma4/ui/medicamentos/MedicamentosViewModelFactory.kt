package com.example.farma4.ui.medicamentos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.database.MedicinaRepository
import java.lang.IllegalArgumentException

class MedicamentosViewModelFactory (private val repository: MedicinaRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicamentosViewModel::class.java)) {
            return MedicamentosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}