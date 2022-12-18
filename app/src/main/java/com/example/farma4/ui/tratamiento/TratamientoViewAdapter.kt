package com.example.farma4.ui.tratamiento

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ListItemTratamientoBinding
import com.example.farma4.tests.Utilidades
import java.util.*

class TratamientoViewAdapter(private val clickListener: (Medicina) -> Unit) :
    RecyclerView.Adapter<TratamientoViewHolder>() {

    private val medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TratamientoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemTratamientoBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item_tratamiento, parent, false)
        return TratamientoViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TratamientoViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int = medicinasList.size

}

class TratamientoViewHolder(val binding: ListItemTratamientoBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {

        val consumo = Utilidades.calcularConsumoDiario(medicina.dosis)
        val nuevoStock = Utilidades.calcularStock(medicina, consumo)
        val cardColor: Int = Utilidades.calcularColor(context, consumo, nuevoStock, medicina)

        binding.cardView.setCardBackgroundColor(cardColor)
        binding.nameTextView.text = medicina.name
        binding.dosisTextView.text = medicina.dosis
        binding.principioText.text = medicina.principio

        binding.listItemLayout.setOnClickListener {
            clickListener(medicina)
        }
    }


}

