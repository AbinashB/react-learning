# Currency Converter API - Client SDKs

This directory contains auto-generated client SDKs for the Currency Converter API in multiple programming languages.

## 📁 Available Clients

### 🔥 **Scala Client** (`/scala`)
- **Framework**: Akka HTTP
- **Package**: `org.openapitools.client`
- **Build Tool**: SBT
- **Usage**: See `USAGE_EXAMPLE.scala`

### 🚀 **Kotlin Client** (`/kotlin`)
- **Framework**: OkHttp4
- **Package**: `com.example.currency.client`
- **Build Tool**: Gradle
- **Usage**: See `USAGE_EXAMPLE.kt`

### 💎 **.NET Core Client** (`/dotnet`)
- **Framework**: .NET 8.0
- **Namespace**: `CurrencyConverter.Client`
- **Build Tool**: dotnet CLI
- **Usage**: See `USAGE_EXAMPLE.cs`

## 🚀 Quick Start

### Scala Client
```bash
cd clients/scala
sbt compile
sbt run
```

### Kotlin Client
```bash
cd clients/kotlin
./gradlew build
./gradlew run
```

### .NET Core Client
```bash
cd clients/dotnet
dotnet build
dotnet run --project src/CurrencyConverter.Client.Test
```

## 📋 API Endpoints

All clients support these endpoints:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/{currencyCode}` | GET | Get currency conversion rates |
| `/api/currencies` | GET | Get supported currencies |
| `/api/health` | GET | Health check |

## 🔧 Configuration

### Base URL
All clients are configured to use `http://localhost:8080` by default. Update the base URL in your code:

**Scala:**
```scala
val basePath = "https://your-api-domain.com"
val api = CurrencyConversionApi(basePath)
```

**Kotlin:**
```kotlin
val basePath = "https://your-api-domain.com"
val api = CurrencyConversionApi(basePath)
```

**.NET:**
```csharp
var configuration = new Configuration
{
    BasePath = "https://your-api-domain.com"
};
var api = new CurrencyConversionApi(configuration);
```

## 📖 Usage Examples

### Get Currency Rates

**Scala:**
```scala
val rates = currencyConversionApi.getCurrencyRates("usd")
rates.onComplete {
  case Success(data) => println(s"USD rates: $data")
  case Failure(ex) => println(s"Error: ${ex.getMessage}")
}
```

**Kotlin:**
```kotlin
val rates = currencyConversionApi.getCurrencyRates("usd")
rates.forEach { (currency, rateMap) ->
    println("$currency rates: $rateMap")
}
```

**.NET:**
```csharp
var rates = await currencyConversionApi.GetCurrencyRatesAsync("usd");
foreach (var pair in rates)
{
    Console.WriteLine($"{pair.Key} rates: {pair.Value}");
}
```

### Get Supported Currencies

**Scala:**
```scala
val currencies = currencyInformationApi.getSupportedCurrencies()
currencies.onComplete {
  case Success(data) => println(s"Supported: ${data.supportedCurrencies}")
  case Failure(ex) => println(s"Error: ${ex.getMessage}")
}
```

**Kotlin:**
```kotlin
val currencies = currencyInformationApi.getSupportedCurrencies()
println("Supported currencies: ${currencies.supportedCurrencies}")
```

**.NET:**
```csharp
var currencies = await currencyInformationApi.GetSupportedCurrenciesAsync();
Console.WriteLine($"Supported currencies: {string.Join(", ", currencies.SupportedCurrencies)}");
```

## 🛠️ Dependencies

### Scala Dependencies
```scala
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10"
)
```

### Kotlin Dependencies
```kotlin
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.moshi:moshi-adapters:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

### .NET Dependencies
```xml
<PackageReference Include="Newtonsoft.Json" Version="13.0.3" />
<PackageReference Include="System.ComponentModel.Annotations" Version="5.0.0" />
```

## 🔄 Error Handling

All clients handle HTTP errors and API exceptions:

- **400 Bad Request**: Invalid currency code
- **404 Not Found**: Endpoint not found
- **500 Internal Server Error**: Server error

### Example Error Handling

**Scala:**
```scala
api.getCurrencyRates("invalid").recover {
  case ex: ApiException => println(s"API Error: ${ex.getMessage}")
  case ex: Exception => println(s"Network Error: ${ex.getMessage}")
}
```

**Kotlin:**
```kotlin
try {
    val rates = api.getCurrencyRates("invalid")
} catch (e: Exception) {
    println("Error: ${e.message}")
}
```

**.NET:**
```csharp
try 
{
    var rates = await api.GetCurrencyRatesAsync("invalid");
}
catch (ApiException ex)
{
    Console.WriteLine($"API Error: {ex.ErrorCode} - {ex.Message}");
}
```

## 📚 Documentation

Each client includes comprehensive documentation:

- **API Documentation**: `/docs` folder in each client
- **Model Documentation**: Generated model classes with inline docs
- **README Files**: Client-specific setup instructions

## 🔧 Regenerating Clients

To regenerate the clients with updated API specifications:

```bash
# Scala
openapi-generator-cli generate -i ../openapi.json -g scala-akka -o scala

# Kotlin  
openapi-generator-cli generate -i ../openapi.json -g kotlin -o kotlin

# .NET
openapi-generator-cli generate -i ../openapi.json -g csharp -o dotnet
```

## 🤝 Contributing

1. Update the OpenAPI specification (`../openapi.json`)
2. Regenerate the clients using the commands above
3. Update usage examples if needed
4. Test the clients against the running API

## 📞 Support

- **API Documentation**: `http://localhost:8080/swagger-ui`
- **OpenAPI Spec**: `http://localhost:8080/api-docs`
- **Health Check**: `http://localhost:8080/api/health`

---

**Generated with OpenAPI Generator v7.14.0**
