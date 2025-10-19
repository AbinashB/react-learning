// Kotlin Usage Example for Currency Converter API Client
// Using OkHttp4 client with Configuration

import com.example.currency.client.apis.CurrencyConversionApi
import com.example.currency.client.apis.CurrencyInformationApi
import com.example.currency.client.apis.HealthCheckApi
import com.example.currency.client.infrastructure.ApiClient
import com.example.currency.client.config.ClientConfig
import kotlinx.coroutines.runBlocking

fun main() {
    println("=== Currency Converter API Client - Kotlin Example ===")
    
    // Print configuration for debugging
    ClientConfig.printConfig()
    
    // Validate configuration
    if (!ClientConfig.validateConfig()) {
        println("Configuration validation failed. Exiting...")
        return
    }
    
    // Configure the API client using configuration
    val basePath = ClientConfig.baseUrl
    val apiClient = ApiClient(basePath = basePath)
    
    // Initialize API clients
    val currencyConversionApi = CurrencyConversionApi(basePath, apiClient.client)
    val currencyInformationApi = CurrencyInformationApi(basePath, apiClient.client)
    val healthCheckApi = HealthCheckApi(basePath, apiClient.client)
    
    runBlocking {
        try {
            // Example 1: Health check
            println("\n1. Health Check:")
            val healthResponse = healthCheckApi.healthCheck()
            println("   Status: ${healthResponse.status}")
            println("   Service: ${healthResponse.service}")
            
            // Example 2: Get supported currencies
            println("\n2. Supported Currencies:")
            val supportedCurrencies = currencyInformationApi.getSupportedCurrencies()
            println("   Count: ${supportedCurrencies.count}")
            println("   Currencies: ${supportedCurrencies.supportedCurrencies.joinToString(", ")}")
            
            // Example 3: Get USD conversion rates
            println("\n3. USD Conversion Rates:")
            val usdRates = currencyConversionApi.getCurrencyRates("usd")
            usdRates.forEach { (currency, rates) ->
                println("   $currency:")
                rates.forEach { (toCurrency, rate) ->
                    println("     $toCurrency: $rate")
                }
            }
            
            // Example 4: Get EUR conversion rates
            println("\n4. EUR Conversion Rates:")
            val eurRates = currencyConversionApi.getCurrencyRates("eur")
            eurRates.forEach { (currency, rates) ->
                println("   $currency:")
                rates.forEach { (toCurrency, rate) ->
                    println("     $toCurrency: $rate")
                }
            }
            
            // Example 5: Error handling - invalid currency
            println("\n5. Error Handling (Invalid Currency):")
            try {
                currencyConversionApi.getCurrencyRates("xyz")
            } catch (e: Exception) {
                println("   Expected error for invalid currency: ${e.message}")
            }
            
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
}

/*
To use this client:

1. Add to build.gradle.kts:
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.moshi:moshi-adapters:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

2. Build and run:
./gradlew build
./gradlew run

3. Or use in your project:
val client = CurrencyConversionApi("http://localhost:8080")
val rates = client.getCurrencyRates("usd")
*/
