package org.example.model

data class CurrencyResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)

data class CurrencyRate(
    val code: String,
    val rate: Double
)
