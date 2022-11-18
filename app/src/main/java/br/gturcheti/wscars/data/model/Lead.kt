package br.gturcheti.wscars.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Lead(
    @PrimaryKey(autoGenerate = true) val leadId: Int = 0,
    @ColumnInfo(name = "car_id") val carId: String,
    @ColumnInfo(name = "client_name") val clientName: String,
    @ColumnInfo(name = "client_phone") val clientPhoneNumber: String,
    @ColumnInfo(name = "client_email") val clientEmail: String,
    @ColumnInfo(name = "created_date") val createDate: Date = Date(),
)
