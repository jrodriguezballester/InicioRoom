package com.example.farma4

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farma4.database.Medicina
import com.example.farma4.databinding.ListItemBinding
import java.text.SimpleDateFormat
import java.util.*


class MedicinaViewAdapter(private val clickListener: (Medicina) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val medicinasList = ArrayList<Medicina>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(medicinasList[position], clickListener)
    }

    override fun getItemCount(): Int = medicinasList.size

    fun setList(medicinaList: List<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
    }
}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(medicina: Medicina, clickListener: (Medicina) -> Unit) {
        val consumo= consumoDiario(medicina.dosis)
        val nuevoStock=calculoStock(medicina,consumo)
     //   val formatter = SimpleDateFormat("dd-MM-yy")
        val fecha = dateToString(medicina.fechaStock)

        binding.nameTextView.text = medicina.name
        binding.dosisTextView.text = medicina.dosis.toString()
        binding.unidadesCajaTextView.text = medicina.unidadesCaja.toString()

        binding.stockTextView.text = nuevoStock.toString()
        binding.consumoTextView.text =consumo.toString()

        binding.fechaTextView.text = fecha
        binding.listItemLayout.setOnClickListener {
            clickListener(medicina)
        }
    }

    private fun dateToString(fechaStock: Date) =
        SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(fechaStock)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculoStock(medicina: Medicina, consumo: Int): Int {
        val dias: Int = calculoPeriodoStock(medicina.fechaStock)
        val consumido:Int=dias*consumo
        val stock=medicina.stock-consumido
        return stock
    }



    private fun calculoPeriodoStock(fechaStock: Date): Int {
        val fechaFinal =Date()
        val dias = ((fechaFinal.time - fechaStock.time) / 86400000).toInt()
        Log.i("MyTAG","Hay $dias dias de diferencia")
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
