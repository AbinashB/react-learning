package org.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.example.config.AppConfig.configureApplication

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    val instanceName = System.getenv("INSTANCE_NAME") ?: "Default"
    
    println("Starting Currency API - $instanceName on port $port")
    
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureApplication()
}
