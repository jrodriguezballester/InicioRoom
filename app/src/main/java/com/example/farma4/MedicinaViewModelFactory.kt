package com.example.farma4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.farma4.database.MedicinaRepository
import java.lang.IllegalArgumentException

class MedicinaViewModelFactory (
    private val repository: MedicinaRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MedicinaViewModel::class.java)){
            return MedicinaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}