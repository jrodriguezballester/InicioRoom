package com.example.farma4.ui.tratamiento


import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
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
    var deletedMedicina = ArrayList<Medicina>()

    fun setList(medicinaList: List<Medicina>, deletedMedicina: ArrayList<Medicina>) {
        medicinasList.clear()
        medicinasList.addAll(medicinaList)
        this.deletedMedicina = deletedMedicina
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TratamientoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemTratamientoBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item_tratamiento, parent, false)
        return TratamientoViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TratamientoViewHolder, position: Int) {
         holder.bind(medicinasList[position], clickListener, deletedMedicina)
    }

    override fun getItemCount(): Int = medicinasList.size

}

class TratamientoViewHolder(val binding: ListItemTratamientoBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    private var isFirstItem: Boolean = true


    fun bind(
        medicina: Medicina, clickListener: (Medicina) -> Unit,
        deletedMedicina: ArrayList<Medicina>,
              ) {
        if (isFirstItem) dimensionarItem()
        if (medicina in deletedMedicina) {
            binding.imagePreparado.visibility = View.VISIBLE
        } else {
            binding.imagePreparado.visibility = View.GONE
        }

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
        binding.listItemLayout.setOnLongClickListener {
            Log.i("MyTAG", "pulsado setOnLongClickListener")

            true
        }
    }



    private fun dimensionarItem() {
        isFirstItem = false
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val cardViewWidth = (screenWidth - 150) / 2

        val params = LayoutParams(cardViewWidth, LayoutParams.WRAP_CONTENT)
        params.bottomMargin = 30  //pixels 10dp=50pixels
        params.width = cardViewWidth
        binding.linearlayout1.layoutParams = params
    }

}

