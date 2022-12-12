package com.example.farma4.ui.inventario

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.R
import com.example.farma4.database.Medicina
import com.example.farma4.databinding.ListItemInventarioBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class InventarioViewAdapter(
    private val context: Context, private val clickListener: (Medicina) -> Unit
) : RecyclerView.Adapter<InventarioViewHolder>() {
    private val medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemInventarioBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item_inventario, parent, false)
        return InventarioViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InventarioViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int = medicinasList.size


}

class InventarioViewHolder(val binding: ListItemInventarioBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {

        val consumoDiario = calcularConsumoDiario(medicina.dosis)
        val consumoSemanal: Double = (consumoDiario * 7)

        val nuevoStock = calcularStock(medicina, consumoDiario)
        val numSemanas: Double = nuevoStock / consumoSemanal
        val fechaFinal: String = calcularDiasFinStock(consumoDiario, nuevoStock)

        val cardColor: Int = calcularColor(consumoSemanal, nuevoStock, medicina)

        binding.cardView.setCardBackgroundColor(cardColor)

        binding.nameTextView.text = medicina.name
        binding.principioText.text = medicina.principio

        binding.unidadesCajaTextView.text = medicina.unidadesCaja.toString()

        binding.stockTextView.text = nuevoStock.toString()
        binding.consumoTextView.text = roundOffOneDecimal(consumoDiario)
        binding.consumoSemanalTextView.text = roundOffZeroDecimal(consumoSemanal)

        binding.numSemTextView.text = roundOffOneDecimal(numSemanas)
        binding.fechaFinalTextView.text = fechaFinal
        binding.listItemLayout.setOnClickListener {
            clickListener(medicina)
        }
    }


    private fun calcularDiasFinStock(consumoDiario: Double, nuevoStock: Int): String {
        val numDias: Int = roundOffZeroDecimalDown((nuevoStock / consumoDiario)).toInt()
        val today = Date()
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DATE, numDias)
        val fechaFinal =dateToString(calendar.time)
        return fechaFinal
    }

    private fun calcularColor(consumoSemanal: Double, stock: Int, medicina: Medicina): Int {
        val colorDeFondo: Int
        val numSemanas: Double = stock / consumoSemanal
//        Log.i(
//            "MyTAG",
//            " ${medicina.name}::stock:${stock} consumoSemanal:${consumoSemanal}::numSemanas${numSemanas}"
//        )
        colorDeFondo = when (numSemanas) {

            in 0.0..1.0 -> ContextCompat.getColor(context, R.color.BlueViolet)
            in 1.0..2.0 -> ContextCompat.getColor(context, R.color.FireBrick)
            in 2.0..3.0 -> ContextCompat.getColor(context, R.color.Peru)
            in 3.0..4.0 -> ContextCompat.getColor(context, R.color.DarkSeaGreen)
            else -> ContextCompat.getColor(context, R.color.DarkGray)
        }

        return colorDeFondo
    }

    private fun dateToString(fechaStock: Date) =
        SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)

    private fun calcularStock(medicina: Medicina, consumoDiario: Double): Int {
        val dias: Int = calculoPeriodoStock(medicina.fechaStock)
        val stock: Int = medicina.stock - (dias * consumoDiario).toInt()
        Log.i(
            "MyTAG",
            "stock Inicial ${medicina.stock} Consumido ${dias * consumoDiario}  nuevo stock ${stock}"
        )
        return stock
    }

    private fun calculoPeriodoStock(fechaStock: Date): Int {
        val fechaFinal = Date()
        val dias = ((fechaFinal.time - fechaStock.time) / 86400000).toInt()
        Log.i("MyTAG", "Hay $dias dias de diferencia")
        return dias
    }

    private fun calcularConsumoDiario(dosis: String): Double {
        Log.i("MyTAG", "dosis ${dosis}::")
        var numComprimidosDiarios: Double = 0.0
        var numComprimidosDosis: Double
        for (num in dosis) {
            numComprimidosDosis = if (num.digitToInt() == 5) 0.5 else num.digitToInt().toDouble()

            numComprimidosDiarios += numComprimidosDosis
        }
  //      Log.i("MyTAG", "numComprimidosDiarios::${numComprimidosDiarios}")
        return numComprimidosDiarios
    }

    private fun roundOffOneDecimal(number: Double): String {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }
    private fun roundOffZeroDecimal(number: Double): String {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }
    private fun roundOffZeroDecimalDown(number: Double): String {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.DOWN
        return df.format(number)
    }
}