package br.gturcheti.wscars.services

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://wswork.com.br/"

private val interceptor = HttpLoggingInterceptor {
    Log.d("RETROFIT_CLIENT", it)
}.apply { level = HttpLoggingInterceptor.Level.BODY }

private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
    .addConverterFactory(GsonConverterFactory.create()).build()

val wsApi: WsApi = retrofit.create(WsApi::class.java)