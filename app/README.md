# Currency Exchange Utility App

This is a utility-style Android app that helps users quickly check currency exchange rates.

## Purpose

The app provides at-a-glance currency conversion information for daily-life use, such as travel, online shopping, or checking foreign prices.

## Features

- Convert an amount between two currencies
- Fetch latest exchange rate data from an external web API
- Display converted amount, exchange rate, and rate date
- Swap source and target currencies
- Settings screen controls decimal display
- Settings screen controls whether major currencies only are shown

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- ViewModel
- Repository pattern
- Simple dependency injection using AppContainer
- Retrofit
- Gson converter

## Architecture

The app separates UI, business logic, and data access:

- `UtilityApp`, `UtilityScreen`, and `SettingsScreen` manage the Compose UI.
- `CurrencyViewModel` manages UI state and user actions.
- `CurrencyRepository` handles currency conversion logic.
- `ExchangeRateApi` defines the Retrofit web API.
- `AppContainer` provides simple dependency injection.

## How to Use

1. Enter an amount.
2. Select the source currency.
3. Select the target currency.
4. Tap Convert.
5. Use Settings to change display options.