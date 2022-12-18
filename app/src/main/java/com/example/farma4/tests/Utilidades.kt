package com.example.farma4.tests

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import java.text.SimpleDateFormat
import java.util.*

class Utilidades {
    companion object {
        fun stringToDate(fechaStockString: String) =
            SimpleDateFormat("dd-MM-yy", Locale.getDefault()).parse(fechaStockString) as Date

        fun stringBarraToDate(fechaStockString: String) =
            SimpleDateFormat("dd/MM/yy", Locale.getDefault()).parse(fechaStockString) as Date

        fun dateToString(fechaStock: Date) =
            SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)

        fun dateToStringBarra(fechaStock: Date) =
            SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(fechaStock)

        fun calcularColor(context: Context, consumo: Double, stock: Int, medicina: Medicina): Int {
            val colorDeFondo: Int
            val numSemanas: Double = stock / (consumo * 7)
            Log.i(
                "MyTAG",
                " ${medicina.name}::stock:${stock} consumo:${consumo}::numSemanas${numSemanas}"
            )
            colorDeFondo = when (numSemanas) {

                in 0.0..1.0 -> ContextCompat.getColor(context, R.color.BlueViolet)
                in 1.0..2.0 -> ContextCompat.getColor(context, R.color.FireBrick)
                in 2.0..3.0 -> ContextCompat.getColor(context, R.color.Peru)
                in 3.0..4.0 -> ContextCompat.getColor(context, R.color.DarkSeaGreen)
                else -> ContextCompat.getColor(context, R.color.DarkGray)
            }

            return colorDeFondo
        }

        fun calcularConsumoDiario(dosis: String): Double {
            Log.i("MyTAG", "dosis ${dosis}::")
            var numComprimidosDiarios = 0.0
            var numComprimidosDosis: Double
            for (num in dosis) {
                numComprimidosDosis =
                    if (num.digitToInt() == 5) 0.5 else num.digitToInt().toDouble()

                numComprimidosDiarios += numComprimidosDosis
            }
            Log.i("MyTAG", "numComprimidosDiarios::${numComprimidosDiarios}")
            return numComprimidosDiarios
        }

        fun calcularStock(medicina: Medicina, consumoDiario: Double): Int {
            val dias: Int = calculoPeriodoStock(medicina.fechaStock)
            val stock: Int = medicina.stock - (dias * consumoDiario).toInt()
            Log.i(
                "MyTAG",
                "stock Inicial ${medicina.stock} Consumido ${dias * consumoDiario}  nuevo stock ${stock}"
            )
            return stock
        }

        fun calculoPeriodoStock(fechaStock: Date): Int {
            val fechaFinal = Date()
            val dias = ((fechaFinal.time - fechaStock.time) / 86400000).toInt()
            Log.i("MyTAG", "Hay $dias dias de diferencia")
            return dias
        }

    }

}
