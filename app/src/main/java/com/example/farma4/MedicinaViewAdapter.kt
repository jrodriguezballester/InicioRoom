package com.example.farma4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.database.Medicina
import com.example.farma4.databinding.ListItemBinding

class MedicinaViewAdapter(private val clickListener: (Medicina) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val medicinasList = ArrayList<Medicina>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int = medicinasList.size

    fun setList(subscribers: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(subscribers)

    }
}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {
        binding.nameTextView.text = medicina.name
        binding.dosisTextView.text = medicina.dosis
        binding.stockTextView.text = medicina.stock.toString()
        binding.unidadesCajaTextView.text = medicina.unidadesCaja.toString()

    }

}
