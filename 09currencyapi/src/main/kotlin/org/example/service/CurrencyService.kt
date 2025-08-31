package org.example.service

import org.example.model.CurrencyResponse
import org.springframework.stereotype.Service

@Service
class CurrencyService {

    // Mock exchange rates - in a real application, you would fetch these from an external API
    private val exchangeRates = mapOf(
        "USD" to mapOf(
            "INR" to 83.25,
            "EUR" to 0.92,
            "GBP" to 0.79,
            "JPY" to 149.50,
            "CNY" to 7.24,
            "CAD" to 1.36,
            "AUD" to 1.53,
            "CHF" to 0.88,
            "SGD" to 1.35,
            "KRW" to 1320.50
        ),
        "EUR" to mapOf(
            "USD" to 1.09,
            "INR" to 90.75,
            "GBP" to 0.86,
            "JPY" to 163.20,
            "CNY" to 7.89,
            "CAD" to 1.48,
            "AUD" to 1.67,
            "CHF" to 0.96,
            "SGD" to 1.47,
            "KRW" to 1440.30
        ),
        "GBP" to mapOf(
            "USD" to 1.27,
            "INR" to 105.50,
            "EUR" to 1.16,
            "JPY" to 189.80,
            "CNY" to 9.18,
            "CAD" to 1.72,
            "AUD" to 1.94,
            "CHF" to 1.12,
            "SGD" to 1.71,
            "KRW" to 1675.40
        ),
        "INR" to mapOf(
            "USD" to 0.012,
            "EUR" to 0.011,
            "GBP" to 0.0095,
            "JPY" to 1.80,
            "CNY" to 0.087,
            "CAD" to 0.016,
            "AUD" to 0.018,
            "CHF" to 0.011,
            "SGD" to 0.016,
            "KRW" to 15.87
        ),
        "JPY" to mapOf(
            "USD" to 0.0067,
            "INR" to 0.56,
            "EUR" to 0.0061,
            "GBP" to 0.0053,
            "CNY" to 0.048,
            "CAD" to 0.0091,
            "AUD" to 0.010,
            "CHF" to 0.0059,
            "SGD" to 0.0090,
            "KRW" to 8.83
        ),
        "CNY" to mapOf(
            "USD" to 0.138,
            "INR" to 11.50,
            "EUR" to 0.127,
            "GBP" to 0.109,
            "JPY" to 20.65,
            "CAD" to 0.188,
            "AUD" to 0.211,
            "CHF" to 0.122,
            "SGD" to 0.186,
            "KRW" to 182.45
        )
    )

    fun getConversionRates(baseCurrency: String): CurrencyResponse? {
        val upperBaseCurrency = baseCurrency.uppercase()
        val rates = exchangeRates[upperBaseCurrency]
        
        return if (rates != null) {
            CurrencyResponse(
                baseCurrency = upperBaseCurrency,
                rates = rates
            )
        } else {
            null
        }
    }

    fun getSupportedCurrencies(): Set<String> {
        return exchangeRates.keys
    }
}
