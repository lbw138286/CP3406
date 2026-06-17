package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.di.AppContainer
import com.example.myapplication.ui.CurrencyViewModel
import com.example.myapplication.ui.CurrencyViewModelFactory
import com.example.myapplication.ui.UtilityApp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val currencyViewModel: CurrencyViewModel = viewModel(
                    factory = CurrencyViewModelFactory(
                        AppContainer.currencyRepository
                    )
                )

                UtilityApp(viewModel = currencyViewModel)
            }
        }
    }
}