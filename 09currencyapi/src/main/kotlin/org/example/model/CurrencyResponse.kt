package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)

@Serializable
data class SupportedCurrenciesResponse(
    val supportedCurrencies: List<String>,
    val count: Int
)

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
    val version: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val supportedCurrencies: List<String>? = null
)
