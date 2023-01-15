package com.example.farma4.database

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.farma4.Utilities.Utilidades
import com.example.farma4.database.fechaStock.fecIniTto
import com.example.farma4.database.fechaStock.fecStock1
import com.example.farma4.database.model.Medicina
import java.util.*


object fechaStock : Date() {

    var fecIniTto =Utilidades.stringBarraToDate("11/11/11")
    var fecStock1:Date =Utilidades.stringBarraToDate("14/01/23")

}

@RequiresApi(Build.VERSION_CODES.O)
val PREPOPULATE_DATA =
    listOf<Medicina>(
        Medicina("Zarelis", "Venlafaxina", "1000", 30, 43,fecStock1,fecIniTto,Utilidades.stringBarraToDate("25/08/23")),
        Medicina("Dormodor", "Flurazepam", "0001", 30, 49, fecStock1,fecIniTto,Utilidades.stringBarraToDate("25/08/23")),
        Medicina("Mirtazapina", "Mirtazapina", "1001", 30, 60, fecStock1,fecIniTto,Utilidades.stringBarraToDate("04/05/23")),
        Medicina("Ziprasidona 60", "---", "0110", 56, 71, fecStock1,fecIniTto,Utilidades.stringBarraToDate("25/08/23")),
        Medicina("Ziprexa", "Olanzapina 5", "0102", 28, 117, fecStock1,fecIniTto,Utilidades.stringBarraToDate("25/08/23")),
        Medicina("Abilify 15", "Aripiprazol 15", "1000", 28, 34, fecStock1,fecIniTto,Utilidades.stringBarraToDate("13/09/23")),
        Medicina("Abilify 5", "Aripiprazol 5", "1000", 28, 28, fecStock1,fecIniTto,Utilidades.stringBarraToDate("23/03/23")),
        Medicina("Etumina", "Clotiapina", "1101", 30, 30,  fecStock1,fecIniTto,Utilidades.stringBarraToDate("25/08/23")),
        Medicina("Tranxilium", "Clorazepato", "1110", 20, 61, fecStock1,fecIniTto,Utilidades.stringBarraToDate("02/06/23")),
        Medicina("Noctamid", "Lormetazepam 2", "0001", 20, 8, fecStock1,fecIniTto,Utilidades.stringBarraToDate("24/02/23")),
        Medicina("Trankimazin", "Alprozolam", "0002", 30, 0,  fecStock1,fecIniTto,Utilidades.stringBarraToDate("15/01/23")),
    )
