package com.example.myapplication.di

import com.example.myapplication.data.CurrencyRepository
import com.example.myapplication.data.ExchangeRateApi
import com.example.myapplication.data.NetworkCurrencyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppContainer {

    private const val BASE_URL = "https://api.frankfurter.dev/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val exchangeRateApi: ExchangeRateApi by lazy {
        retrofit.create(ExchangeRateApi::class.java)
    }

    val currencyRepository: CurrencyRepository by lazy {
        NetworkCurrencyRepository(exchangeRateApi)
    }
}