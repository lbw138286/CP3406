package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class CurrencyUiState(
    val amountInput: String = "100",
    val fromCurrency: String = "USD",
    val toCurrency: String = "AUD",
    val resultText: String = "",
    val rateText: String = "",
    val dateText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val roundToTwoDecimals: Boolean = true,
    val showMajorCurrenciesOnly: Boolean = true
)

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState = _uiState.asStateFlow()

    val majorCurrencies = listOf(
        "AUD", "USD", "EUR", "GBP", "JPY", "CNY", "SGD", "NZD", "CAD"
    )

    val allCurrencies = listOf(
        "AUD", "USD", "EUR", "GBP", "JPY", "CNY", "SGD", "NZD", "CAD",
        "CHF", "HKD", "KRW", "INR", "MYR", "THB", "PHP", "SEK", "NOK", "MXN", "ZAR"
    )

    fun updateAmountInput(value: String) {
        _uiState.update {
            it.copy(
                amountInput = value,
                errorMessage = null
            )
        }
    }

    fun updateFromCurrency(currency: String) {
        _uiState.update {
            it.copy(
                fromCurrency = currency,
                errorMessage = null
            )
        }
    }

    fun updateToCurrency(currency: String) {
        _uiState.update {
            it.copy(
                toCurrency = currency,
                errorMessage = null
            )
        }
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency,
                resultText = "",
                rateText = "",
                dateText = "",
                errorMessage = null
            )
        }
    }

    fun setRoundToTwoDecimals(value: Boolean) {
        _uiState.update {
            it.copy(roundToTwoDecimals = value)
        }
    }

    fun setShowMajorCurrenciesOnly(value: Boolean) {
        val allowedCurrencies = if (value) majorCurrencies else allCurrencies

        _uiState.update { currentState ->
            currentState.copy(
                showMajorCurrenciesOnly = value,
                fromCurrency = if (currentState.fromCurrency in allowedCurrencies) {
                    currentState.fromCurrency
                } else {
                    "USD"
                },
                toCurrency = if (currentState.toCurrency in allowedCurrencies) {
                    currentState.toCurrency
                } else {
                    "AUD"
                }
            )
        }
    }

    fun convertCurrency() {
        val currentState = _uiState.value
        val amount = currentState.amountInput.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _uiState.update {
                it.copy(errorMessage = "Enter a valid amount greater than 0.")
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = repository.convertCurrency(
                amount = amount,
                fromCurrency = currentState.fromCurrency,
                toCurrency = currentState.toCurrency
            )

            result.onSuccess { conversion ->
                val decimalPlaces = if (_uiState.value.roundToTwoDecimals) 2 else 4
                val convertedText = String.format(
                    Locale.US,
                    "%.${decimalPlaces}f %s",
                    conversion.convertedAmount,
                    conversion.toCurrency
                )

                val rateText = String.format(
                    Locale.US,
                    "1 %s = %.4f %s",
                    conversion.fromCurrency,
                    conversion.rate,
                    conversion.toCurrency
                )

                _uiState.update {
                    it.copy(
                        resultText = convertedText,
                        rateText = rateText,
                        dateText = "Rate date: ${conversion.date}",
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Could not load exchange rate."
                    )
                }
            }
        }
    }
}

class CurrencyViewModelFactory(
    private val repository: CurrencyRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}