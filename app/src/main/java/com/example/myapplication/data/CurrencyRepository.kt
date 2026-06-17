package com.example.myapplication.data

data class CurrencyConversion(
    val originalAmount: Double,
    val convertedAmount: Double,
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val date: String
)

interface CurrencyRepository {
    suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Result<CurrencyConversion>
}

class NetworkCurrencyRepository(
    private val api: ExchangeRateApi
) : CurrencyRepository {

    override suspend fun convertCurrency(
        amount: Double,
        fromCurrency: String,
        toCurrency: String
    ): Result<CurrencyConversion> {
        return try {
            if (fromCurrency == toCurrency) {
                return Result.success(
                    CurrencyConversion(
                        originalAmount = amount,
                        convertedAmount = amount,
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency,
                        rate = 1.0,
                        date = "Same currency"
                    )
                )
            }

            val response = api.getLatestRate(
                amount = amount,
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            )

            val convertedAmount = response.rates[toCurrency]
                ?: return Result.failure(Exception("Currency rate not found."))

            val rate = convertedAmount / response.amount

            Result.success(
                CurrencyConversion(
                    originalAmount = response.amount,
                    convertedAmount = convertedAmount,
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    rate = rate,
                    date = response.date
                )
            )
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}