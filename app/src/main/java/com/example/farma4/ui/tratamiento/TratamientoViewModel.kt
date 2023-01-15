package com.example.farma4.ui.tratamiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.farma4.Utilities.Event
import com.example.farma4.database.MedicinaRepository
import com.example.farma4.database.model.Medicina

class TratamientoViewModel(private val repository: MedicinaRepository) : ViewModel() {

    private lateinit var medicina: Medicina
   // val deletedMedicina = ArrayList<Medicina>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = statusMessage

//    val desayunoList: MutableList<Medicina> = mutableListOf<Medicina>()
//    val comidaList: MutableList<Medicina> = mutableListOf<Medicina>()
//    val cenaList: MutableList<Medicina> = mutableListOf<Medicina>()
//    val resoponList: MutableList<Medicina> = mutableListOf<Medicina>()


    fun getSavedMedicinas() = liveData {
        repository.medicinas.collect {
            emit(it)
        }
    }

}