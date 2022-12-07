package com.example.farma4.ui.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.database.MedicinaRepository
import java.lang.IllegalArgumentException

class InventarioViewModelFactory(
    private val repository: MedicinaRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InventarioViewModel::class.java)){
            return InventarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}