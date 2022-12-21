package com.example.farma4.database

import androidx.room.*
import com.example.farma4.database.model.Medicina
import kotlinx.coroutines.flow.Flow


@Dao
interface MedicinaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedicina(medicina: Medicina): Long

    @Update
    suspend fun updateMedicina(medicina: Medicina): Int

    @Delete
    suspend fun deleteMedicina(medicina: Medicina): Int

    @Query("DELETE FROM medicina_data_table")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM medicina_data_table")
    fun getAllMedicinas(): Flow<List<Medicina>>

    @Query("SELECT * FROM medicina_data_table ORDER BY FecFinTto ASC")
    fun getAllMedicinasByFecFinTto(): Flow<List<Medicina>>

    @Query("SELECT * FROM medicina_data_table")
    fun getListMedicinas(): List<Medicina>

}