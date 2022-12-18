package com.example.farma4.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "Medicina_data_table")
data class Medicina(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "principio")
    var principio: String,
    @ColumnInfo(name = "dosis")
    var dosis: String,
    @ColumnInfo(name = "unidadescaja")
    var unidadesCaja: Int,
    @ColumnInfo(name = "stock")
    var stock: Int,
    @ColumnInfo(name = "fechaStock")
    var fechaStock: Date
//    FecIniTto
//    FecFinTto

)
//{
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Int? = null
//}
