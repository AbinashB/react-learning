package org.example.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

/**
 * Custom logging interceptor for detailed request/response logging
 */
object LoggingInterceptor {
    
    private val logger = LoggerFactory.getLogger("RequestResponseLogger")
    
    fun Application.configureRequestResponseLogging() {
        val instanceName = System.getenv("INSTANCE_NAME") ?: "Default"
        
        // Intercept requests
        intercept(ApplicationCallPipeline.Monitoring) {
            val startTime = System.currentTimeMillis()
            val method = call.request.httpMethod.value
            val uri = call.request.uri
            val remoteHost = call.request.local.remoteHost
            val queryParams = call.request.queryParameters.entries()
                .joinToString(", ") { (key, values) -> "$key=${values.joinToString(",")}" }
            
            // Log incoming request
            logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            logger.info("📥 INCOMING REQUEST [$instanceName]")
            logger.info("   Method      : $method")
            logger.info("   URI         : $uri")
            logger.info("   Remote Host : $remoteHost")
            
            if (queryParams.isNotEmpty()) {
                logger.info("   Query Params: $queryParams")
            }
            
            // Log headers
            val importantHeaders = listOf("User-Agent", "Content-Type", "Accept", "Authorization")
            importantHeaders.forEach { headerName ->
                call.request.headers[headerName]?.let { headerValue ->
                    val displayValue = if (headerName == "Authorization") {
                        headerValue.take(20) + "..."
                    } else {
                        headerValue
                    }
                    logger.info("   $headerName: $displayValue")
                }
            }
            
            try {
                // Process the request
                val executionTime = measureTimeMillis {
                    proceed()
                }
                
                // Log response
                val status = call.response.status() ?: HttpStatusCode.OK
                val statusEmoji = when (status.value) {
                    in 200..299 -> "✅"
                    in 300..399 -> "↪️"
                    in 400..499 -> "⚠️"
                    in 500..599 -> "❌"
                    else -> "❓"
                }
                
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                logger.info("📤 OUTGOING RESPONSE [$instanceName]")
                logger.info("   Status      : $statusEmoji ${status.value} ${status.description}")
                logger.info("   Execution   : ${executionTime}ms")
                logger.info("   Path        : $method $uri")
                logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                
            } catch (e: Exception) {
                val executionTime = System.currentTimeMillis() - startTime
                
                logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                logger.error("❌ REQUEST FAILED [$instanceName]")
                logger.error("   Path        : $method $uri")
                logger.error("   Execution   : ${executionTime}ms")
                logger.error("   Error       : ${e.message}")
                logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                throw e
            }
        }
    }
    
    /**
     * Extension function to log specific business logic events
     */
    fun ApplicationCall.logBusinessEvent(event: String, details: Map<String, Any> = emptyMap()) {
        val instanceName = System.getenv("INSTANCE_NAME") ?: "Default"
        logger.info("🔔 BUSINESS EVENT [$instanceName]: $event")
        if (details.isNotEmpty()) {
            details.forEach { (key, value) ->
                logger.info("   $key: $value")
            }
        }
    }
}

