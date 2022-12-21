package com.example.farma4.database.model

import com.example.farma4.tests.Utilidades

interface Mapper {

    fun MedFormTOMedicina(medForm: MedForm): Medicina
}

/// val responseMapeada = userlist.map { MapperImpl.toUserModel(it) }
object MapperImpl : Mapper {

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

    //    fun MedFormTOMedicina(medForm: MedForm, medClicked: Medicina): Medicina {
//        return Medicina(
//            medForm.name,
//            medForm.principio,
//            medForm.dosis,
//            medForm.unidadesCaja,
//            medClicked.stock,
//            medClicked.fechaStock,
//            medForm
//        )
//    }
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
}