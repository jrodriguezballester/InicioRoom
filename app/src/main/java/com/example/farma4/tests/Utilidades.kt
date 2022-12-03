package com.example.farma4.tests

import java.text.SimpleDateFormat
import java.util.*

class Utilidades {
    companion object {
        fun stringToDate(fechaStockString: String) =
            SimpleDateFormat("dd-MM-yy", Locale.getDefault()).parse(fechaStockString) as Date

        fun dateToString(fechaStock: Date) =
            SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)


    }
}