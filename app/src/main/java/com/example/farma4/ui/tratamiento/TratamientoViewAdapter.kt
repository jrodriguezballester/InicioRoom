package com.example.farma4.ui.tratamiento

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
import com.example.farma4.databinding.ListItemHorizontalBinding
import java.text.SimpleDateFormat
import java.util.*

class TratamientoViewAdapter(
    private val context: Context, private val clickListener: (Medicina) -> Unit
) : RecyclerView.Adapter<TratamientoViewHolder>() {
    private val medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TratamientoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemHorizontalBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item_horizontal, parent, false)
        return TratamientoViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TratamientoViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int = medicinasList.size

}

class TratamientoViewHolder(val binding: ListItemHorizontalBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {

        val consumo = consumoDiario(medicina.dosis)
        val nuevoStock = calculoStock(medicina, consumo)
        val fecha = dateToString(medicina.fechaStock)
        val cardColor: Int = calcularColor(consumo, nuevoStock)

        binding.cardView.setCardBackgroundColor(cardColor)
        binding.nameTextView.text = medicina.name
        binding.dosisTextView.text = medicina.dosis.toString()
        binding.unidadesCajaTextView.text = medicina.unidadesCaja.toString()

        binding.stockTextView.text = nuevoStock.toString()
        binding.consumoTextView.text = consumo.toString()

        binding.fechaTextView.text = fecha

        binding.listItemLayout.setOnClickListener {
            clickListener(medicina)
        }
    }

    private fun calcularColor(consumo: Int, stock: Int): Int {
        var colorDeFondo: Int = ContextCompat.getColor(context, R.color.PowderBlue)

        if ((stock - (consumo * 7)) < 0) colorDeFondo =
            ContextCompat.getColor(context, R.color.FireBrick)
        if ((stock - (consumo * 7 * 2)) < 0) colorDeFondo =
            ContextCompat.getColor(context, R.color.Peru)
        if ((stock - (consumo * 7 * 3)) < 0) colorDeFondo =
            ContextCompat.getColor(context, R.color.DarkSeaGreen)

    return colorDeFondo
}

private fun dateToString(fechaStock: Date) =
    SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)

@RequiresApi(Build.VERSION_CODES.O)
private fun calculoStock(medicina: Medicina, consumo: Int): Int {
    val dias: Int = calculoPeriodoStock(medicina.fechaStock)
    val consumido: Int = dias * consumo
    val stock = medicina.stock - consumido
    return stock
}


private fun calculoPeriodoStock(fechaStock: Date): Int {
    val fechaFinal = Date()
    val dias = ((fechaFinal.time - fechaStock.time) / 86400000).toInt()
    Log.i("MyTAG", "Hay $dias dias de diferencia")
    return dias
}

private fun consumoDiario(dosis: String): Int {
    //     val numberString: String = dosis.toString()
    Log.i("MyTAG", "consumo ${dosis}::")
    var consumo: Int = 0
    for (num in dosis) {
        var sumar = num.digitToInt()
        // TODO Controlar medias dosis
        consumo = consumo + sumar
        Log.i("MyTAG", "numero ${num}::${dosis}")
    }
    return consumo
}

}