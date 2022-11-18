package br.gturcheti.wscars.services.dto

import com.google.gson.annotations.SerializedName

data class CarsDTO(
    val id: Int,
    @SerializedName("marca_nome")
    val marca: String,
    @SerializedName("nome_modelo")
    val modelo: String,
    val ano: Int,
    val combustivel: String,
    @SerializedName("num_portas")
    val portas: Int,
    @SerializedName("valor_fipe")
    val valor: Any,
    val cor: String,
)
