package org.example.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.model.*
import org.example.service.CurrencyService

class CurrencyController(private val currencyService: CurrencyService) {
    
    fun configureRoutes(routing: Routing) {
        routing.route("/api") {
                // Get currency conversion rates
                get("/{currencyCode}") {
                    val currencyCode = call.parameters["currencyCode"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Invalid request", "Currency code is required")
                    )
                    
                    val response = currencyService.getConversionRates(currencyCode)
                    
                    if (response != null) {
                        // Format response as requested: {usd: {inr: 1, cny: 2, etc}}
                        val formattedResponse = mapOf(
                            currencyCode.lowercase() to response.rates.mapKeys { it.key.lowercase() }
                        )
                        call.respond(HttpStatusCode.OK, formattedResponse)
                    } else {
                        val errorResponse = ErrorResponse(
                            error = "Currency not supported",
                            message = "The currency code '$currencyCode' is not supported",
                            supportedCurrencies = currencyService.getSupportedCurrencies().map { it.lowercase() }
                        )
                        call.respond(HttpStatusCode.BadRequest, errorResponse)
                    }
                }
                
                // Get supported currencies
                get("/currencies") {
                    val supportedCurrencies = currencyService.getSupportedCurrencies().map { it.lowercase() }
                    val response = SupportedCurrenciesResponse(
                        supportedCurrencies = supportedCurrencies,
                        count = supportedCurrencies.size
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
                
                // Health check
                get("/health") {
                    val response = HealthResponse(
                        status = "UP",
                        service = "Currency Converter API"
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
        }
    }
}
