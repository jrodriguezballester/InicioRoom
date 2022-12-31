package com.example.farma4.ui.tratamiento

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ActivityTratamientoBinding
import kotlin.collections.ArrayList


class TratamientoActivity : AppCompatActivity() {

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

        initDesayunoRecyclerView()
        initComidaRecyclerView()
        initCenaRecyclerView()
        initResoponRecyclerView()

    }

    private fun initDesayunoRecyclerView() {
        binding.desayunoRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterDesayuno =
            TratamientoViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.desayunoRecyclerView.adapter = adapterDesayuno
        desayunoMedicinasList()
    }

    private fun initComidaRecyclerView() {
        binding.comidaRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterComida =
            TratamientoViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.comidaRecyclerView.adapter = adapterComida
        comidaMedicinaList()
    }

    private fun initCenaRecyclerView() {
        binding.cenaRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterCena =
            TratamientoViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.cenaRecyclerView.adapter = adapterCena
        cenaMedicinasList()
    }

    private fun initResoponRecyclerView() {
        binding.resoponRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterResopon =
            TratamientoViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.resoponRecyclerView.adapter = adapterResopon
        resoponMedicinasList()
    }

    private fun desayunoMedicinasList() {
        val cero: String = "0"
        var ceroChar = cero[0]
        ttoViewModel.getSavedMedicinas().observe(this, Observer {
            for (medicina in it)
                if (medicina.dosis[0] != ceroChar) ttoViewModel.desayunoList.add(medicina)
            Log.i("MyTAG", "desayunoMedicinasList ${ttoViewModel.desayunoList}")
            adapterDesayuno.setList(ttoViewModel.desayunoList)
            adapterDesayuno.notifyDataSetChanged()
        })
    }

    private fun comidaMedicinaList() {
        val cero: String = "0"
        var ceroChar = cero[0]
        ttoViewModel.getSavedMedicinas().observe(this, Observer {
            for (medicina in it) {
                if (medicina.dosis[1] != ceroChar) ttoViewModel.comidaList.add(medicina)
            }
            Log.i("MyTAG", "comidaMedicinasList ${ttoViewModel.comidaList}")
            adapterComida.setList(ttoViewModel.comidaList)
            adapterComida.notifyDataSetChanged()
        })
    }

    private fun cenaMedicinasList() {
        val cero: String = "0"
        var ceroChar = cero[0]
        ttoViewModel.getSavedMedicinas().observe(this, Observer {
            for (medicina in it)
                if (medicina.dosis[2] != ceroChar) ttoViewModel.cenaList.add(medicina)
            Log.i("MyTAG", "desayunoMedicinasList ${ttoViewModel.cenaList}")
            adapterCena.setList(ttoViewModel.cenaList)
            adapterCena.notifyDataSetChanged()
        })
    }

    private fun resoponMedicinasList() {
        val cero: String = "0"
        var ceroChar = cero[0]
        ttoViewModel.getSavedMedicinas().observe(this, Observer {
            for (medicina in it)
                if (medicina.dosis[3] != ceroChar) ttoViewModel.resoponList.add(medicina)
            Log.i("MyTAG", "desayunoMedicinasList ${ttoViewModel.resoponList}")
            adapterResopon.setList(ttoViewModel.resoponList)
            adapterResopon.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MyTAG", "pulsado listItemClicked")
        //   Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        mostrarAlertConDosis(medicina, this)
//
    }

    private fun mostrarAlertConDosis(medicina: Medicina, context: Context) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Recuerda esta en:")
        val items: Array<String> = calculaArrayDosis(medicina)
        builder.setItems(items) { dialog, which ->
            when (which) {
                0, 1, 2, 3 -> {
                    Log.i("Inventario", "pulsado OK -${which}--------")
                    ttoViewModel.deletedMedicina.add(medicina)
                    eliminarItems()
                }
                else -> {
                    Log.i("Inventario", "pulsado OK -${which}--------")
                    ttoViewModel.deletedMedicina.add(medicina)
                    eliminarItems()
                }
            }
        }
        // create and show the alert dialog
        builder.create().show()

    }

    private fun calculaArrayDosis(medicina: Medicina): Array<String> {
        val cero: String = "0"
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
            Log.i("calculaArrayDosis", "${arrayDosis.toString()}-------")
        }
        val array = arrayDosis.toTypedArray()
        return array
    }

    fun eliminarItems() {
        for (medicina in ttoViewModel.deletedMedicina) {
            if (medicina in ttoViewModel.desayunoList) ttoViewModel.desayunoList.remove(medicina)
            if (medicina in ttoViewModel.comidaList) ttoViewModel.comidaList.remove(medicina)
            if (medicina in ttoViewModel.cenaList) ttoViewModel.cenaList.remove(medicina)
            if (medicina in ttoViewModel.resoponList) ttoViewModel.resoponList.remove(medicina)
        }
        adapterDesayuno.setList(ttoViewModel.desayunoList)
        adapterComida.setList(ttoViewModel.comidaList)
        adapterCena.setList(ttoViewModel.cenaList)
        adapterResopon.setList(ttoViewModel.resoponList)

        adapterDesayuno.notifyDataSetChanged()
        adapterComida.notifyDataSetChanged()
        adapterCena.notifyDataSetChanged()
        adapterResopon.notifyDataSetChanged()

    }

}
//
//    private fun calcularDestinoListas(dosis: String, medicina: Medicina) {
//        Log.i("MyTAG", "${medicina.name}, dosis ${dosis}::")
//        var indice = 0
//        var cero: String = "0"
//        var ceroChar = cero[0]
//        ttoViewModel.desayunoList.clear()
//        ttoViewModel.comidaList.clear()
//        ttoViewModel.cenaList.clear()
//        ttoViewModel.resoponList.clear()
//        for (num in dosis) {
//            indice += 1
//            if (num != ceroChar) {
//                when (indice) {
//                    1 -> ttoViewModel.desayunoList.add(medicina)
//                    2 -> ttoViewModel.comidaList.add(medicina)
//                    3 -> ttoViewModel.cenaList.add(medicina)
//                    4 -> ttoViewModel.resoponList.add(medicina)
//
//                }
//            }
//        }
//
//        Log.i("MyTAG ", "desayuno  ${ttoViewModel.desayunoList}::")
//        Log.i("MyTAG ", "comida  ${ttoViewModel.comidaList}::")
//        Log.i("MyTAG ", "cena  ${ttoViewModel.cenaList}::")
//        Log.i("MyTAG ", "resopon  ${ttoViewModel.resoponList}::")
//
//    }


