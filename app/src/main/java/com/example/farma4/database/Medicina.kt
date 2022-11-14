package com.example.farma4.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Medicina_data_table")
data class Medicina(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "dosis")
    var dosis: String,
    @ColumnInfo(name = "unidadescaja")
    var unidadesCaja: Int,
    @ColumnInfo(name = "stock")
    var stock: Int

)
//{
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Int? = null
//}
