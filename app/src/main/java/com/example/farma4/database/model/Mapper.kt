package com.example.farma4.database.model

import com.example.farma4.Utilities.Utilidades

interface Mapper {

    fun MedTOMedTope(med: Medicina, numSemanas: Double, fechaFinal: String): MedTope

    fun MedFormTOMedicina(medForm: MedForm): Medicina
}

/// val responseMapeada = userlist.map { MapperImpl.toUserModel(it) }
object MapperImpl : Mapper {
    override fun MedTOMedTope(medicina: Medicina, numSemanas: Double, fechaFinal: String): MedTope {
        return MedTope(medicina, numSemanas, fechaFinal)
    }

    override fun MedFormTOMedicina(medForm: MedForm): Medicina {
        return Medicina(
            medForm.name,
            medForm.principio,
            medForm.dosis,
            medForm.unidadesCaja,
            0,
            medForm.fecStock,
            medForm.fecIniTto,
            medForm.fecFinTto
        )
    }

    fun MedNameTOMedicina(name: String): Medicina {
        return Medicina(
            name,
            "-",
            "0000",
            0,
            0,
            Utilidades.stringBarraToDate("1/1/1"),
            Utilidades.stringBarraToDate("1/1/1"),
            Utilidades.stringBarraToDate("1/1/1")

        )
    }

    fun MedTopeTOMed(medTope:MedTope): Medicina {
        return Medicina(  medTope.medicina.name,
            medTope.medicina.principio,
            medTope.medicina.dosis,
            medTope.medicina.unidadesCaja,
            medTope.medicina.stock,
            medTope.medicina.fechaStock,
            medTope.medicina.FecIniTto,
            medTope.medicina.FecFinTto
            )
    }

}