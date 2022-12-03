package com.example.farma4.database

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object fechaStock : Date() {

    val dates = "20221127"

    @RequiresApi(Build.VERSION_CODES.O)
    val fechaStock1 = LocalDate.parse(dates, DateTimeFormatter.BASIC_ISO_DATE)

    @RequiresApi(Build.VERSION_CODES.O)
    var fechaStockDate = Date.from(fechaStock1.atStartOfDay(ZoneId.systemDefault()).toInstant())

}

@RequiresApi(Build.VERSION_CODES.O)
val PREPOPULATE_DATA =
    listOf<Medicina>(
        Medicina("Zarelis", "Venlafaxina", "1000", 30, 60, fechaStock.fechaStockDate),
        Medicina("Dormodor", "Fluracepam", "0001", 30, 36, fechaStock.fechaStockDate),
        Medicina("Mirtazapina", "Mirtazapina", "1001", 30, 64, fechaStock.fechaStockDate),
        Medicina("Ziprasidona 60", "-", "0110", 56, 57, fechaStock.fechaStockDate),
        Medicina("Ziprexa", "Olanzapina 5", "0102", 28, 87, fechaStock.fechaStockDate),
        Medicina("Abilify 15", "Aripiprazol 15", "1000", 28, 53, fechaStock.fechaStockDate),
        Medicina("Abilify 5", "Aripiprazol 5", "1000", 28, 45, fechaStock.fechaStockDate),
        Medicina("Etumina", "Clotiapina", "1101", 30, 75, fechaStock.fechaStockDate),
        Medicina("Tranxilium", "Clorazepato", "5110", 20, 59, fechaStock.fechaStockDate),
        Medicina("Noctamid", "Lormetazepam 2", "0001", 20, 41, fechaStock.fechaStockDate),
        Medicina("Trankimazin", "Alprozolam", "0002", 30, 42, fechaStock.fechaStockDate),
    )