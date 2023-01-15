package com.example.farma4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.farma4.ui.inventario.InventarioActivity
import com.example.farma4.ui.medicamentos.MedicamentosActivity
import com.example.farma4.ui.prescripcion.PrescripcionActivity
import com.example.farma4.ui.tratamiento.TratamientoActivity

open class BaseActivity: AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemTto -> {
                startActivity<TratamientoActivity>()
                return true
            }
            R.id.itemInventario-> {
                startActivity<InventarioActivity>()
                return true
            }
            R.id.itemMedicinas-> {
                startActivity<MedicamentosActivity>()
                return true
            }
            R.id.itemRecetas-> {
                startActivity<PrescripcionActivity>()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    inline fun <reified T : Activity> Context.createIntent() =
        Intent(this, T::class.java)

    inline fun <reified T : Activity> Activity.startActivity() {
        startActivity(createIntent<T>())
    }
}