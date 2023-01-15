package com.example.farma4

import com.example.farma4.database.model.Medicina

class DatosSingleton private constructor() {
    val desayunoList: MutableList<Medicina> = mutableListOf<Medicina>()
    val comidaList: MutableList<Medicina> = mutableListOf<Medicina>()
    val cenaList: MutableList<Medicina> = mutableListOf<Medicina>()
    val resoponList: MutableList<Medicina> = mutableListOf<Medicina>()
    val deletedMedicina = ArrayList<Medicina>()

    companion object {
        private var INSTANCE:DatosSingleton? = null
        fun getInstance(): DatosSingleton {
            if (INSTANCE == null) {
                INSTANCE = DatosSingleton()
            }
            return INSTANCE!!
        }
    }
//    fun setData(data: String) {
//        this.data = data
//    }
//    fun getData(): String {
//        return data
//    }
}