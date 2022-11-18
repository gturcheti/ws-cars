package br.gturcheti.wscars.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {
    @Insert
    fun insert(lead: Lead)

    @Query("SELECT * FROM Lead")
    fun getAllLeads(): Flow<List<Lead>>

}