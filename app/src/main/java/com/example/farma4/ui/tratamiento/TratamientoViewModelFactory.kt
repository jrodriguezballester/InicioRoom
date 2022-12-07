package com.example.farma4.ui.tratamiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.database.MedicinaRepository
import java.lang.IllegalArgumentException

class TratamientoViewModelFactory(
    private val repository: MedicinaRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TratamientoViewModel::class.java)){
            return TratamientoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}