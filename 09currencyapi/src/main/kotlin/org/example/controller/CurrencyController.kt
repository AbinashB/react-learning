package org.example.controller

import org.example.model.CurrencyResponse
import org.example.service.CurrencyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CurrencyController(private val currencyService: CurrencyService) {

    @GetMapping("/{currencyCode}")
    fun getCurrencyRates(@PathVariable currencyCode: String): ResponseEntity<Any> {
        val response = currencyService.getConversionRates(currencyCode)
        
        return if (response != null) {
            // Format response as requested: {usd: {inr: 1, cny: 2, etc}}
            val formattedResponse = mapOf(currencyCode.lowercase() to response.rates.mapKeys { it.key.lowercase() })
            ResponseEntity.ok(formattedResponse)
        } else {
            val errorResponse = mapOf(
                "error" to "Currency not supported",
                "message" to "The currency code '$currencyCode' is not supported",
                "supportedCurrencies" to currencyService.getSupportedCurrencies().map { it.lowercase() }
            )
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }
    }

    @GetMapping("/currencies")
    fun getSupportedCurrencies(): ResponseEntity<Map<String, Any>> {
        val supportedCurrencies = currencyService.getSupportedCurrencies().map { it.lowercase() }
        val response = mapOf(
            "supportedCurrencies" to supportedCurrencies,
            "count" to supportedCurrencies.size
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "Currency Converter API"))
    }
}
