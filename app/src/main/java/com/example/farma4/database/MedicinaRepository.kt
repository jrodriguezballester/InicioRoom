package com.example.farma4.database

import com.example.farma4.database.model.Medicina

class MedicinaRepository(private val medicinaDAO: MedicinaDAO) {

    val medicinas = medicinaDAO.getAllMedicinas()
    val medicinasOrderByFecFinTto = medicinaDAO.getAllMedicinasByFecFinTto()

    suspend fun insert(medicina: Medicina): Long {
        return medicinaDAO.insertMedicina(medicina)
    }

    suspend fun update(medicina: Medicina): Int {
        return medicinaDAO.updateMedicina(medicina)
    }

    suspend fun delete(medicina: Medicina): Int {
        return medicinaDAO.deleteMedicina(medicina)
    }

    suspend fun deleteAll(): Int {
        return medicinaDAO.deleteAll()
    }

}