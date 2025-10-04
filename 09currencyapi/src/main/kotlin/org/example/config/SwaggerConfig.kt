package org.example.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

object SwaggerConfig {
    
    fun Routing.configureSwaggerRoutes() {
        // Serve OpenAPI specification from classpath
        get("/api-docs") {
            try {
                val inputStream = this::class.java.classLoader.getResourceAsStream("openapi.json")
                if (inputStream != null) {
                    val openApiContent = inputStream.bufferedReader().use { it.readText() }
                    call.respondText(openApiContent, ContentType.Application.Json)
                } else {
                    call.respondText("OpenAPI specification not found in classpath", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Error reading OpenAPI specification: ${e.message}", status = HttpStatusCode.InternalServerError)
            }
        }
        
        get("/openapi.yaml") {
            val openApiFile = File("openapi.yaml")
            if (openApiFile.exists()) {
                call.respondText(openApiFile.readText(), ContentType.Text.Plain)
            } else {
                call.respondText("OpenAPI specification not found", status = HttpStatusCode.NotFound)
            }
        }
        
        // Serve Swagger UI HTML
        get("/swagger-ui") {
            val swaggerHtml = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Currency Converter API - Swagger UI</title>
                    <link rel="stylesheet" type="text/css" href="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui.css" />
                    <style>
                        html { box-sizing: border-box; overflow: -moz-scrollbars-vertical; overflow-y: scroll; }
                        *, *:before, *:after { box-sizing: inherit; }
                        body { margin:0; background: #fafafa; }
                    </style>
                </head>
                <body>
                    <div id="swagger-ui"></div>
                    <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-bundle.js"></script>
                    <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-standalone-preset.js"></script>
                    <script>
                        window.onload = function() {
                            const ui = SwaggerUIBundle({
                                url: '/api-docs',
                                dom_id: '#swagger-ui',
                                deepLinking: true,
                                presets: [
                                    SwaggerUIBundle.presets.apis,
                                    SwaggerUIStandalonePreset
                                ],
                                plugins: [
                                    SwaggerUIBundle.plugins.DownloadUrl
                                ],
                                layout: "StandaloneLayout"
                            });
                        };
                    </script>
                </body>
                </html>
            """.trimIndent()
            
            call.respondText(swaggerHtml, ContentType.Text.Html)
        }
        
        // Redirect /swagger to /swagger-ui
        get("/swagger") {
            call.respondRedirect("/swagger-ui")
        }
    }
}
