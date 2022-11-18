package br.gturcheti.wscars.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import br.gturcheti.wscars.App
import br.gturcheti.wscars.data.model.Lead
import br.gturcheti.wscars.services.dto.CarsDTO
import br.gturcheti.wscars.services.wsApi
import br.gturcheti.wscars.ui.vo.CarsVO
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.NumberFormat

class HomeViewModel : ViewModel() {

    private val _carsList: MutableLiveData<Result<List<CarsVO>>> = MutableLiveData()
    val carsList: LiveData<Result<List<CarsVO>>> = _carsList

    fun fetchCars() {
        _carsList.value = Result.Loading
        viewModelScope.launch {
            try {
                wsApi.fetchCars().let { response ->
                    val vo = response.mapCars()
                    _carsList.value = Result.Success(vo)
                }
            } catch (ex: Exception) {
                _carsList.value = Result.Error
            }
        }
    }

    fun searchCars(query: String) {
        _carsList.value = Result.Loading
        viewModelScope.launch {
            try {
                wsApi.fetchCars().let { response ->
                    val vo = response.mapCars().searchFor(query)
                    _carsList.value = Result.Success(vo)
                }
            } catch (ex: Exception) {
                _carsList.value = Result.Error
            }
        }
    }

     fun sendLeads(leads: List<Lead>, lifecycleOwner: LifecycleOwner) {
        val postResponse: LiveData<Response<Any>> = liveData {
            val response = wsApi.sendLeads(leads)
            emit(response)
        }
        postResponse.observe(lifecycleOwner) {
            Log.d("RESPONSE", it.body().toString())
        }
    }

    private fun List<CarsVO>.searchFor(query: String): List<CarsVO> =
        this.filter { cars ->
            cars.modelo.lowercase().contains(query.lowercase()) || cars.marca.lowercase()
                .contains(query.lowercase())
        }

    private fun List<CarsDTO>.mapCars(): List<CarsVO> {
        return this.map { carsDTO ->
            CarsVO(
                id = carsDTO.id.toString(),
                marca = carsDTO.marca.uppercase(),
                modelo = carsDTO.modelo.uppercase(),
                ano = carsDTO.ano.toString(),
                combustivel = carsDTO.combustivel.uppercase(),
                portas = carsDTO.portas.toString(),
                valor = carsDTO.valor.formatValue(),
                cor = carsDTO.cor.uppercase()
            )
        }
    }

    private fun Any.formatValue(): String {
        val numberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return when (this) {
            is String -> return numberFormat.format(this.toInt())
            is Double -> return numberFormat.format(this * 1000)
            else -> "Consulte"
        }
    }

}


