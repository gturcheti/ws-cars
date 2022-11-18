package br.gturcheti.wscars.services

import br.gturcheti.wscars.data.model.Lead
import br.gturcheti.wscars.services.dto.CarsDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WsApi {

    @GET("cars.json")
    suspend fun fetchCars() : List<CarsDTO>

    @POST("cars/leads/")
    suspend fun sendLeads(@Body lead: List<Lead>) : Response<Any>
}