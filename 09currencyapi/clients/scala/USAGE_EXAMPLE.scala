// Scala Usage Example for Currency Converter API Client
// Using Akka HTTP client

import org.openapitools.client.api.{CurrencyConversionApi, CurrencyInformationApi, HealthCheckApi}
import org.openapitools.client.core.ApiInvoker
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}

object CurrencyConverterExample extends App {
  
  implicit val system: ActorSystem = ActorSystem("currency-converter-client")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  
  // Configure the API client
  val basePath = "http://localhost:8080"
  val apiInvoker = ApiInvoker(basePath)
  
  // Initialize API clients
  val currencyConversionApi = CurrencyConversionApi(basePath)
  val currencyInformationApi = CurrencyInformationApi(basePath)
  val healthCheckApi = HealthCheckApi(basePath)
  
  // Example 1: Get currency conversion rates
  def getCurrencyRates(currencyCode: String): Future[Map[String, Any]] = {
    currencyConversionApi.getCurrencyRates(currencyCode)
  }
  
  // Example 2: Get supported currencies
  def getSupportedCurrencies(): Future[org.openapitools.client.model.SupportedCurrenciesResponse] = {
    currencyInformationApi.getSupportedCurrencies()
  }
  
  // Example 3: Health check
  def healthCheck(): Future[org.openapitools.client.model.HealthResponse] = {
    healthCheckApi.healthCheck()
  }
  
  // Usage examples
  println("=== Currency Converter API Client - Scala Example ===")
  
  // Test health check
  healthCheck().onComplete {
    case Success(health) => 
      println(s"Health Status: ${health.status}")
      println(s"Service: ${health.service}")
    case Failure(exception) => 
      println(s"Health check failed: ${exception.getMessage}")
  }
  
  // Get USD conversion rates
  getCurrencyRates("usd").onComplete {
    case Success(rates) => 
      println("USD Conversion Rates:")
      rates.foreach { case (currency, rate) => 
        println(s"  $currency: $rate")
      }
    case Failure(exception) => 
      println(s"Failed to get USD rates: ${exception.getMessage}")
  }
  
  // Get supported currencies
  getSupportedCurrencies().onComplete {
    case Success(currencies) => 
      println(s"Supported Currencies (${currencies.count}):")
      currencies.supportedCurrencies.foreach(currency => println(s"  - $currency"))
    case Failure(exception) => 
      println(s"Failed to get supported currencies: ${exception.getMessage}")
  }
  
  // Shutdown system after operations
  Thread.sleep(5000) // Wait for async operations
  system.terminate()
}

/*
To use this client:

1. Add to build.sbt:
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10"
)

2. Run:
sbt compile
sbt run
*/
