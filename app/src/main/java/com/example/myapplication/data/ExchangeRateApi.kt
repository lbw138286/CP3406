package com.example.myapplication.data

import retrofit2.http.GET
import retrofit2.http.Query

data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getLatestRate(
        @Query("amount") amount: Double,
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String
    ): ExchangeRateResponse
}