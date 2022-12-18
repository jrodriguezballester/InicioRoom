package com.example.farma4.ui.medicamentos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ItemMedicamentosBinding
import java.util.ArrayList

class MedicamentosViewAdapter(private val clickListener: (Medicina) -> Unit) :
    RecyclerView.Adapter<MedicamentosViewHolder>() {

    private val medicinasList = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMedicamentosBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_medicamentos, parent, false)
        return MedicamentosViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MedicamentosViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return medicinasList.size
    }

}

class MedicamentosViewHolder(val binding: ItemMedicamentosBinding, context: Context) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {
        binding.nameTextView.text=medicina.name

        binding.listItemLayout.setOnClickListener {
            clickListener(medicina)
        }
    }

}
