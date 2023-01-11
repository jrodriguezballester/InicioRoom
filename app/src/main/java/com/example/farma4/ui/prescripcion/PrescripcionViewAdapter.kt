package com.example.farma4.ui.prescripcion

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ItemPrescripcionBinding
import com.example.farma4.tests.Utilidades
import java.util.*

class PrescripcionViewAdapter : RecyclerView.Adapter<PrescripcionViewHolder>() {

    private val medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescripcionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPrescripcionBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_prescripcion, parent, false)
        return PrescripcionViewHolder(binding, parent.context)

    }

    override fun onBindViewHolder(holder: PrescripcionViewHolder, position: Int) {
        holder.bind(medicinasList[position])
    }

    override fun getItemCount(): Int {
        return medicinasList.size
    }

}

class PrescripcionViewHolder(val binding: ItemPrescripcionBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(medicina: Medicina) {

        val diasToEndTto: Long = (medicina.FecFinTto.getTime() - Date().getTime()) / (3600000 * 24)
        binding.nameTextView.text = medicina.name
        binding.principioTextView.text = medicina.principio

        binding.diasFinTtoTextView.text = diasToEndTto.toString()
        binding.fecFinTtoTextView.text = Utilidades.dateToStringBarra(medicina.FecFinTto)

        val cardColor: Int = Utilidades.calcularColorTto(context, diasToEndTto)
        binding.cardView.setCardBackgroundColor(cardColor)

    }


}
