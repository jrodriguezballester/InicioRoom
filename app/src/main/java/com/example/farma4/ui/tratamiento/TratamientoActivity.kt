package com.example.farma4.ui.tratamiento

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.farma4.BaseActivity
import com.example.farma4.DatosSingleton.Companion.getInstance
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ActivityTratamientoBinding

class TratamientoActivity : BaseActivity() {

    private lateinit var binding: ActivityTratamientoBinding
    private lateinit var ttoViewModel: TratamientoViewModel

    private lateinit var adapterDesayuno: TratamientoViewAdapter
    private lateinit var adapterComida: TratamientoViewAdapter
    private lateinit var adapterCena: TratamientoViewAdapter
    private lateinit var adapterResopon: TratamientoViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tratamiento)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tratamiento)

        // crear viewModel
        val factory = TratamientoViewModelFactory(MyApp.medicinaRepository!!)
        ttoViewModel = ViewModelProvider(this, factory)[TratamientoViewModel::class.java]

        // vincular xml con viewmodel
        binding.myViewModel = ttoViewModel
        binding.lifecycleOwner = this

        //Observar mensajes
        ttoViewModel.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let { its ->
                Log.i("MyTAG", "toast ${its}")
                Toast.makeText(this, its, Toast.LENGTH_LONG).show()
            }
        }
        calcularDestinoListas()

        initDesayunoRecyclerView()
        initComidaRecyclerView()
        initCenaRecyclerView()
        initResoponRecyclerView()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapters() {
        adapterDesayuno.setList(getInstance().desayunoList, getInstance().deletedMedicina)
        adapterComida.setList(getInstance().comidaList, getInstance().deletedMedicina)
        adapterCena.setList(getInstance().cenaList, getInstance().deletedMedicina)
        adapterResopon.setList(getInstance().resoponList, getInstance().deletedMedicina)

        adapterDesayuno.notifyDataSetChanged()
        adapterComida.notifyDataSetChanged()
        adapterCena.notifyDataSetChanged()
        adapterResopon.notifyDataSetChanged()
    }

    private fun initDesayunoRecyclerView() {
        binding.desayunoRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterDesayuno = TratamientoViewAdapter(::listItemClicked)
        binding.desayunoRecyclerView.adapter = adapterDesayuno
    }

    private fun initComidaRecyclerView() {
        binding.comidaRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterComida = TratamientoViewAdapter(::listItemClicked)
        binding.comidaRecyclerView.adapter = adapterComida

    }

    private fun initCenaRecyclerView() {
        binding.cenaRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterCena = TratamientoViewAdapter(::listItemClicked)
        binding.cenaRecyclerView.adapter = adapterCena
    }

    private fun initResoponRecyclerView() {
        binding.resoponRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterResopon = TratamientoViewAdapter(::listItemClicked)
        binding.resoponRecyclerView.adapter = adapterResopon

    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado listItemClicked")
        mostrarAlertConDosis(medicina, this)
    }

       /**
     * Muestra Alert Diciendo la medicina y la dosis de la misma
     * marca la medicina en cada recycler
     */
    private fun mostrarAlertConDosis(medicina: Medicina, context: Context) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Recuerda ${medicina.name} esta en:")
        val items: Array<String> = calculaArrayDosis(medicina)

        builder.setItems(items) { dialog, which ->
            when (which) {
                0, 1, 2, 3 -> {
                    Log.i("MyTAG Inventario", "pulsado OK -${which}--------")
                    getInstance().deletedMedicina.add(medicina)
                    initAdapters()
                }
                else -> {
                    Log.i("MyTAG Inventario", "pulsado OK -${which}--------")
                    getInstance().deletedMedicina.add(medicina)
                }
            }
        }
        // create and show the alert dialog
        builder.create().show()

    }

    /**
     * Calcula el array de String de la dosis del parametro medicina
     */
    private fun calculaArrayDosis(medicina: Medicina): Array<String> {
        val cero = "0"
        val ceroChar = cero[0]
        var indice = 0
        val items = arrayOf("Desayuno", "Comida", "Cena", "Resopon")
        val arrayDosis: ArrayList<String> = ArrayList<String>()
        for (num in medicina.dosis) {
            if (num != ceroChar) {
                when (indice) {
                    0 -> arrayDosis.add(items[indice])
                    1 -> arrayDosis.add(items[indice])
                    2 -> arrayDosis.add(items[indice])
                    3 -> arrayDosis.add(items[indice])
                    else -> arrayDosis.add("--")
                }
            }
            indice += 1
            Log.i("MyTAG calculaArrayDosis", "${arrayDosis.toString()}-------")
        }
        val array = arrayDosis.toTypedArray()
        return array
    }

    private fun calcularDestinoListas() {
        ttoViewModel.getSavedMedicinas().observe(this) {
            getInstance().comidaList.clear()
            getInstance().desayunoList.clear()
            getInstance().cenaList.clear()
            getInstance().resoponList.clear()
            for (medicina in it) {
                Log.i("MyTAG __", "${medicina.name}, dosis ${medicina.dosis}::")
                var indice = 0
                val cero: String = "0"
                val ceroChar = cero[0]

                for (num in medicina.dosis) {
                    indice += 1
                    if (num != ceroChar) {
                        when (indice) {
                            1 -> getInstance().desayunoList.add(medicina)
                            2 -> getInstance().comidaList.add(medicina)
                            3 -> getInstance().cenaList.add(medicina)
                            4 -> getInstance().resoponList.add(medicina)
                        }
                    }
                }

            }
            initAdapters()
        }
    }
}


