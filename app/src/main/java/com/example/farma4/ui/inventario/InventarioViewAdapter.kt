package com.example.farma4.ui.inventario

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.R
import com.example.farma4.database.model.MapperImpl
import com.example.farma4.database.model.MedTope
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ListItemInventarioBinding
import com.example.farma4.tests.Utilidades.Companion.calcularColor
import com.example.farma4.tests.Utilidades.Companion.calcularConsumoDiario
import com.example.farma4.tests.Utilidades.Companion.calcularDiasFinStock
import com.example.farma4.tests.Utilidades.Companion.calcularStock
import com.example.farma4.tests.Utilidades.Companion.roundOffOneDecimal
import com.example.farma4.tests.Utilidades.Companion.roundOffZeroDecimal
import java.util.*

class InventarioViewAdapter(
    private val clickListener: (Medicina) -> Unit
) : RecyclerView.Adapter<InventarioViewHolder>() {
    private var medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }
    fun setList2(mylistOrd: List<MedTope>) {
        medicinasList.clear()
        medicinasList= mylistOrd.map {MapperImpl.MedTopeTOMed(it)  } as ArrayList<Medicina>
//        medicinasList.map {  }
//        medicinasList.addAll(medicinaList)
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
        val actualStock = calcularStock(medicina, consumoDiario)

        val numSemanas: Double = actualStock / consumoSemanal
        val fechaFinal: String = calcularDiasFinStock(consumoDiario, actualStock)
        val cardColor: Int = calcularColor(context, consumoDiario, actualStock, medicina)

        binding.apply {
            cardView.setCardBackgroundColor(cardColor)
            nameTextView.text = medicina.name
            principioText.text = medicina.principio
            unidadesCajaTextView.text = medicina.unidadesCaja.toString()
            stockTextView.text = actualStock.toString()
            consumoTextView.text = roundOffOneDecimal(consumoDiario)
            consumoSemanalTextView.text = roundOffZeroDecimal(consumoSemanal)
            numSemTextView.text = roundOffOneDecimal(numSemanas)
            fechaFinalTextView.text = fechaFinal
            listItemLayout.setOnClickListener {
                clickListener(medicina)
            }
        }
    }


}


