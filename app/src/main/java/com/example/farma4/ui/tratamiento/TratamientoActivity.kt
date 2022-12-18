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
import java.util.*


class TratamientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTratamientoBinding
    private lateinit var ttoViewModel: TratamientoViewModel

    // private lateinit var adapter: MedicinaViewAdapter
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

        //   initRecyclerView()
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
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        //     tratamientoViewModel.clickMedicina()

        withMultiChoiceList2(medicina, this)
    }

    fun withMultiChoiceList2(medicina: Medicina, context: Context) {
        var cero: String = "0"
        var ceroChar = cero[0]
        var indice = 0
        var items = arrayOf("Desayuno", "Comida", "Cena", "Resopon")

        for (num in medicina.dosis) {
            indice += 1
            if (num == ceroChar) {
                when (indice) {
                    1 -> items[indice - 1] = "-"
                    2 -> items[indice - 1] = "-"
                    3 -> items[indice - 1] = "-"
                    4 -> items[indice - 1] = "-"
                }
            }
        }


        val selectedList = java.util.ArrayList<Int>()
        val builder = AlertDialog.Builder(context)

        builder.setTitle("This is list choice dialog box")
        builder.setMultiChoiceItems(items, null)
        { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("DONE") { dialogInterface, i ->
            Log.i("Inventario", "pulsado OK -${selectedList}--------")

            ttoViewModel.deletedMedicina.add(medicina)

            val selectedStrings = java.util.ArrayList<String>()

            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])
            }
            Log.i("Inventario", "pulsado OK -${selectedStrings}--------")

            Toast.makeText(
                context,
                "Items selected are: " + Arrays.toString(selectedStrings.toTypedArray()),
                Toast.LENGTH_LONG
            ).show()
            eliminarItems()
        }
        builder.show()
    }


    private fun initRecyclerView() {
        binding.desayunoRecyclerView.layoutManager = GridLayoutManager(this, 2)
        adapterDesayuno =
            TratamientoViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.desayunoRecyclerView.adapter = adapterDesayuno
        //    displayMedicinasList()
    }

    private fun displayMedicinasList() {
        ttoViewModel.getSavedMedicinas().observe(this, Observer {
            adapterDesayuno.setList(it)
            adapterDesayuno.notifyDataSetChanged()
        })
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