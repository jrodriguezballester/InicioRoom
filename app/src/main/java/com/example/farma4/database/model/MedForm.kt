package com.example.farma4.database.model

import java.util.*

data class MedForm(

    var name: String,
    var principio: String,
    var dosis: String,
    var unidadesCaja: Int,

    var fecIniTto: Date,
    var fecFinTto: Date,

    var stock:Int,
    var fecStock:Date
)
