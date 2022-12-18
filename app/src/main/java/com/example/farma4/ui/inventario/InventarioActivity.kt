package com.example.farma4.ui.inventario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farma4.MyApp
import com.example.farma4.R
import com.example.farma4.database.model.Medicina
import com.example.farma4.databinding.ActivityInventarioBinding

class InventarioActivity : AppCompatActivity(), InventarioDialogFragment.InventarioDialogListener {
    private lateinit var binding: ActivityInventarioBinding
    private lateinit var adapter: InventarioViewAdapter
    private lateinit var inventarioViewModel: InventarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)
        Log.i("Inventario", "XXXXXX")
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
        adapter =
            InventarioViewAdapter(this, { selectedItem: Medicina -> listItemClicked(selectedItem) })
        binding.inventarioRecyclerView.adapter = adapter
        displayMedicinasList()
    }

    private fun listItemClicked(medicina: Medicina) {
        Log.i("Inventario", "pulsado")
        Toast.makeText(this, "selected name is ${medicina.name}", Toast.LENGTH_LONG).show()
        showdialog(medicina)
    }

    private fun displayMedicinasList() {
        inventarioViewModel.getSavedMedicinas().observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    fun showdialog(medicina: Medicina) {
        val newFragment = InventarioDialogFragment(medicina)
        newFragment.show(supportFragmentManager, "game")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, numCajas: Int, medicina: Medicina) {
        Log.i("Inventario", "pulsado OK ${medicina.name}:${numCajas}")
        inventarioViewModel.addCajas(numCajas, medicina)
    }

}
