package com.example.farma4

import android.app.Application
import android.util.Log
import com.example.farma4.database.MedicinaDAO
import com.example.farma4.database.MedicinaDatabase
import com.example.farma4.database.MedicinaRepository

class MyApp:Application() {
   companion object{

       var medicinaDAO: MedicinaDAO? =null
       var medicinaRepository: MedicinaRepository?=null

   }



    override fun onCreate() {
        super.onCreate()
        medicinaDAO = MedicinaDatabase.getInstance(this)!!.medicinaDAO
        Log.i("MyTAG", ": medicinaDAO::${medicinaDAO}")
        medicinaRepository = MedicinaRepository(medicinaDAO!!)

    }


}