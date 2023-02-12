package com.example.farma4.ui.inventario

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.BaseActivity
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.Utilities.Utilidades
import com.example.farma4.database.model.MapperImpl
import com.example.farma4.database.model.MedTope
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ActivityInventarioBinding

class InventarioActivity : BaseActivity(), InventarioDialogFragment.InventarioDialogListener {
    private lateinit var binding: ActivityInventarioBinding
    private lateinit var adapter: InventarioViewAdapter
    private lateinit var inventarioViewModel: InventarioViewModel
    private lateinit var subList: List<MedTope>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)
        Log.i("MyTAG Inventario", "XXXXXX")
        val factory = InventarioViewModelFactory(MyApp.medicinaRepository!!)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventario)
        inventarioViewModel = ViewModelProvider(this, factory).get(InventarioViewModel::class.java)
        //Observar mensajes
        inventarioViewModel.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let { its ->
                Log.i("MyTAG", "toast ${its}")
                Toast.makeText(this, its, Toast.LENGTH_LONG).show()
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.inventarioRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InventarioViewAdapter { selectedItem: Medicina -> listItemClicked(selectedItem) }
        binding.inventarioRecyclerView.adapter = adapter
        displayMedicinasList2()

    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("MyTAG Inventario", "pulsado")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        showdialog(medicina)
    }

    private fun displayMedicinasList() {
        inventarioViewModel.getSavedMedicinas().observe(this) {
            val mylist = it.sortedBy { it.name }
            adapter.setList(mylist)
            adapter.notifyDataSetChanged()
        }
    }

    private fun displayMedicinasList2() {
        val myList = mutableListOf<MedTope>()
        inventarioViewModel.getSavedMedicinas().observe(this) {
            myList.clear()
            for (medicina in it) {
                val consumoDiario = Utilidades.calcularConsumoDiario(medicina.dosis)
                val consumoSemanal: Double = (consumoDiario * 7)
                val actualStock = Utilidades.calcularStock(medicina, consumoDiario)
                val numSemanas: Double = actualStock / consumoSemanal
                val fechaFinal: String = Utilidades.calcularDiasFinStock(consumoDiario, actualStock)
                val medTope = MapperImpl.MedTOMedTope(medicina, numSemanas, fechaFinal)
                myList.add(medTope)
            }
            val mylistOrd: List<MedTope> = myList.sortedBy { it.numSemanas }
            subList = mylistOrd.filter { it.numSemanas <= 4 }

            adapter.setList2(mylistOrd)
            adapter.notifyDataSetChanged()
        }
    }


    fun showdialog(medicina: Medicina) {
        val newFragment = InventarioDialogFragment(medicina, subList)
        newFragment.show(supportFragmentManager, "game")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, numCajas: Int, medicina: Medicina) {
        Log.i("MyTAG Inventario", "pulsado OK ${medicina.name}:${numCajas}")
        inventarioViewModel.addCajas(numCajas, medicina)
    }

    override fun mandarMensaje(dialog: Dialog?) {

        Log.i("MyTAG __Inventario", "captura")

        val listaAComprar = subList.map { it.medicina.name }
        Log.i("MyTAG __Inventario", "message::$listaAComprar")

        val phoneNumber = "+34605011868" // Número de teléfono del destinatario

        val urgentes = subList.filter { it.numSemanas <= 2 }.map { it.medicina.name }
        val resto = subList.filter { it.numSemanas > 2 }.map { it.medicina.name }

        val message = "Falta Comprar Urgente:\n $urgentes \n y si salen:\n $resto"

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
        )
        startActivity(intent)
        Log.i("MyTAG __Inventario", "lanzar wasap")

    }
}
