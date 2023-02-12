package com.example.farma4.ui.inventario

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.farma4.R
import com.example.farma4.database.model.MedTope
import com.example.farma4.database.model.Medicina

class InventarioDialogFragment(private val medicina: Medicina, private val subList: List<MedTope>) :
    DialogFragment() {
    // Use this instance of the interface to deliver action events
    internal lateinit var listener: InventarioDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface InventarioDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, numCajas: Int, medicina: Medicina)
        fun mandarMensaje(dialog: Dialog?)
        //    fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as InventarioDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement InventarioDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val listItems = arrayOf("Caja 1", "Caja 2", "Mandar lista por wasap")
            var numCajas = 1
            builder.setTitle(medicina.name)
            builder.setSingleChoiceItems(listItems, 0) { dialogInterface, i ->
                when (i) {
                    0, 1 -> {
                        Log.i("MyTAG Dialog", "pulsado::${i}")
                        numCajas += i
                    }
                    2 -> {
                        Log.i("MyTAG Dialog", "pulsadosssss::${i}")
                       listener.mandarMensaje(dialog)
                        //   numCajas += i
                    }
                }
            }
                .setPositiveButton(R.string.aceptar) { dialog, id ->
                    Log.i("MyTAG Dialog", "numCajas::${numCajas}")
                    listener.onDialogPositiveClick(this, numCajas, medicina)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    //  listener.onDialogNegativeClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}
