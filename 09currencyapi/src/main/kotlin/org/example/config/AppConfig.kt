package org.example.config

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.config.PluginConfig.configurePlugins
import org.example.config.SwaggerConfig.configureSwaggerRoutes
import org.example.config.LoggingInterceptor.configureRequestResponseLogging
import org.example.controller.CurrencyController
import org.example.service.CurrencyService

object AppConfig {
    
    fun Application.configureServices() {
        // Initialize services
        val currencyService = CurrencyService()
        val currencyController = CurrencyController(currencyService)
        
        // Configure routes
        routing {
            // API routes
            currencyController.configureRoutes(this)
            
            // Swagger/OpenAPI routes
            configureSwaggerRoutes()
        }
    }
    
    fun Application.configureApplication() {
        // Configure all application components
        configurePlugins()
        configureRequestResponseLogging() // Add detailed request/response logging
        configureServices()
    }
}
