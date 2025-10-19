# Currency Converter API - Kotlin Client

A Kotlin client library for the Currency Converter API, generated using OpenAPI Generator.

## 📋 **Overview**

- **API Version**: 1.0.0
- **Package**: `com.example.currency.client`
- **Generator**: OpenAPI Generator 7.14.0
- **HTTP Client**: OkHttp4
- **JSON Serialization**: Moshi
- **Async Support**: Kotlin Coroutines

## 🔧 **Requirements**

- **Kotlin**: 1.9.23+
- **Gradle**: 8.14.3+
- **Java**: 17+ (recommended: Java 17 or 21)

> **Note**: If you encounter Java version compatibility issues, make sure you're using Java 17 or 21. Java 22+ may have compatibility issues with some Gradle versions.

## 🚀 **Quick Start**

### **1. Build the Client**

```bash
# Navigate to the Kotlin client directory
cd clients/kotlin

# Make gradlew executable (if needed)
chmod +x gradlew

# Build the project
./gradlew build

# Run tests
./gradlew test

# Clean and rebuild
./gradlew clean build
```

> **Troubleshooting**: If you get Java version errors, try:
> ```bash
> # Check Java version
> java -version
> 
> # If using Java 22+, switch to Java 17 or 21
> # On macOS with SDKMAN:
> sdk install java 17.0.9-tem
> sdk use java 17.0.9-tem
> 
> # Then try building again
> ./gradlew clean build
> ```

### **2. Configuration**

#### **Option A: Configuration File**
Edit `src/main/resources/application.properties`:
```properties
# API Base URL
currency.api.base-url=http://localhost:8080

# HTTP Settings
currency.api.timeout=30000
currency.api.connection-timeout=10000
```

#### **Option B: Environment Variables**
```bash
export CURRENCY_API_BASE_URL="http://localhost:8080"
export CURRENCY_API_TIMEOUT=30000
```

### **3. Example Usage**

Create a simple Kotlin file:

```kotlin
import com.example.currency.client.apis.CurrencyConversionApi
import com.example.currency.client.apis.CurrencyInformationApi
import com.example.currency.client.apis.HealthCheckApi
import com.example.currency.client.config.ClientConfig
import kotlinx.coroutines.runBlocking

fun main() {
    println("🚀 Currency Converter API - Kotlin Client")
    
    // Print current configuration
    ClientConfig.printConfig()
    
    // Validate configuration
    if (!ClientConfig.validateConfig()) {
        println("❌ Configuration validation failed!")
        return
    }
    
    // Create API clients
    val baseUrl = ClientConfig.baseUrl
    val healthApi = HealthCheckApi(baseUrl)
    val currencyApi = CurrencyConversionApi(baseUrl)
    val infoApi = CurrencyInformationApi(baseUrl)
    
    runBlocking {
        try {
            // 1. Health Check
            println("\n🏥 Health Check:")
            val health = healthApi.healthCheck()
            println("   Status: ${health.status}")
            println("   Service: ${health.service}")
            
            // 2. Get Supported Currencies
            println("\n💰 Supported Currencies:")
            val currencies = infoApi.getSupportedCurrencies()
            println("   Count: ${currencies.count}")
            println("   Currencies: ${currencies.supportedCurrencies.joinToString(", ")}")
            
            // 3. Get USD Exchange Rates
            println("\n💵 USD Exchange Rates:")
            val usdRates = currencyApi.getCurrencyRates("usd")
            usdRates.forEach { (currency, rates) ->
                println("   $currency:")
                rates.forEach { (toCurrency, rate) ->
                    println("     $toCurrency: $rate")
                }
            }
            
            println("\n✅ All operations completed successfully!")
            
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
```

## 🌐 **Example URLs**

### **Development (Local)**
```bash
export CURRENCY_API_BASE_URL="http://localhost:8080"
```

### **Staging**
```bash
export CURRENCY_API_BASE_URL="https://api-staging.example.com"
```

### **Production**
```bash
export CURRENCY_API_BASE_URL="https://api.example.com"
```

### **Custom Port**
```bash
export CURRENCY_API_BASE_URL="http://localhost:3000"
```

### **HTTPS with Custom Domain**
```bash
export CURRENCY_API_BASE_URL="https://currency-api.yourdomain.com"
```

## 🏗️ **Build Commands**

### **Using the Run Script (Recommended)**
```bash
# Make the script executable (first time only)
chmod +x run-client.sh

# Build the client
./run-client.sh build

# Run tests
./run-client.sh test

# Build and run the example
./run-client.sh run

# Run with custom URL
./run-client.sh run --url https://api.example.com

# Run with custom timeout
./run-client.sh run --url http://localhost:3000 --timeout 60000

# Clean build artifacts
./run-client.sh clean

# Show help
./run-client.sh help
```

### **Direct Gradle Commands**
```bash
./gradlew build                 # Build the project
./gradlew clean                 # Clean build artifacts
./gradlew clean build          # Clean and rebuild
./gradlew runExample           # Run the usage example
```

### **Testing**
```bash
./gradlew test                 # Run all tests
./gradlew check               # Run tests and code quality checks
./gradlew test --info        # Run tests with detailed output

# Or using the run script
./run-client.sh test           # Run tests
./run-client.sh test --verbose # Run tests with verbose output
```

### **Code Quality**
```bash
./gradlew spotlessCheck       # Check code formatting
./gradlew spotlessApply       # Apply code formatting
```

### **Publishing**
```bash
./gradlew publishToMavenLocal  # Publish to local Maven repository
./gradlew jar                  # Create JAR file
```

## 📦 **Using in Your Project**

### **Option 1: Include as Dependency**

Add to your `build.gradle.kts`:
```kotlin
dependencies {
    // Core dependencies
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.moshi:moshi-adapters:1.15.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // If you publish the client to a repository
    implementation("com.example.currency:client:1.0.0")
}
```

### **Option 2: Copy Source Files**

Copy the generated source files to your project:
```bash
cp -r src/main/kotlin/com/example/currency/client/* your-project/src/main/kotlin/
```

## 📚 **API Documentation**

### **Available APIs**

| API Class | Description | Endpoints |
|-----------|-------------|-----------|
| `CurrencyConversionApi` | Get currency exchange rates | `GET /api/{currencyCode}` |
| `CurrencyInformationApi` | Get supported currencies | `GET /api/currencies` |
| `HealthCheckApi` | Health check | `GET /api/health` |

### **Model Classes**

| Model | Description |
|-------|-------------|
| `ErrorResponse` | Error response format |
| `HealthResponse` | Health check response |
| `SupportedCurrenciesResponse` | Supported currencies list |

## 🔧 **Configuration Options**

### **All Available Properties**

```properties
# API Base URL
currency.api.base-url=http://localhost:8080

# HTTP Client Settings
currency.api.timeout=30000
currency.api.connection-timeout=10000
currency.api.max-connections=100

# Retry Settings
currency.api.retry.max-attempts=3
currency.api.retry.initial-delay=1000
currency.api.retry.max-delay=10000

# Logging
currency.api.logging.level=INFO
currency.api.logging.log-requests=false
currency.api.logging.log-responses=false
```

### **Environment Variable Overrides**

| Property | Environment Variable |
|----------|---------------------|
| Base URL | `CURRENCY_API_BASE_URL` |
| Timeout | `CURRENCY_API_TIMEOUT` |
| Connection Timeout | `CURRENCY_API_CONNECTION_TIMEOUT` |

## 🛠️ **Development Workflow**

### **1. Start the API Server**
```bash
# In the main project directory
./gradlew run
# Server starts at http://localhost:8080
```

### **2. Test the Client**
```bash
# In the client directory
cd clients/kotlin
./gradlew build
./gradlew test
```

### **3. Run Example**
```bash
# Create and run your example file
kotlin -cp build/libs/* YourExample.kt
```

## 🐛 **Troubleshooting**

### **Java Version Issues**

**Problem**: `Unsupported class file major version` error
```bash
# Check your Java version
java -version

# If using Java 22+, switch to Java 17 or 21
# Option 1: Using SDKMAN (recommended)
curl -s "https://get.sdkman.io" | bash
sdk install java 17.0.9-tem
sdk use java 17.0.9-tem

# Option 2: Using Homebrew (macOS)
brew install openjdk@17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home

# Option 3: Download from Oracle/OpenJDK
# Visit: https://adoptium.net/temurin/releases/
```

### **Build Issues**

1. **Gradle Wrapper Not Executable**
   ```bash
   chmod +x gradlew
   ./gradlew build
   ```

2. **Build Fails with Dependencies**
   ```bash
   ./gradlew clean build --refresh-dependencies
   ./gradlew dependencies --configuration runtimeClasspath
   ```

3. **Out of Memory**
   ```bash
   export GRADLE_OPTS="-Xmx2g -XX:MaxMetaspaceSize=512m"
   ./gradlew clean build
   ```

### **Runtime Issues**

1. **Connection Refused**
   ```bash
   # Check if API server is running
   curl http://localhost:8080/api/health
   
   # Start the API server (from project root)
   cd ../../
   ./gradlew run
   ```

2. **Configuration Not Found**
   ```bash
   # Check if config file exists
   ls -la src/main/resources/application.properties
   
   # Print current configuration in your code
   ClientConfig.printConfig()
   ```

3. **SSL/HTTPS Issues**
   ```bash
   # For development, you can disable SSL verification
   # (NOT recommended for production)
   export CURRENCY_API_BASE_URL="http://localhost:8080"
   ```

### **Testing Issues**

1. **Tests Fail**
   ```bash
   ./gradlew test --info
   ./gradlew test --debug
   ```

2. **Network Tests Fail**
   ```bash
   # Run tests without network-dependent tests
   ./gradlew test -Dtest.profile=offline
   ```

### **Debug Configuration**

Add this to your code to debug configuration:
```kotlin
fun debugConfig() {
    println("=== Configuration Debug ===")
    ClientConfig.printConfig()
    
    if (!ClientConfig.validateConfig()) {
        println("❌ Configuration is invalid!")
    } else {
        println("✅ Configuration is valid!")
    }
}
```

## 📝 **Example Scenarios**

### **Scenario 1: Get USD to EUR Rate**
```kotlin
val api = CurrencyConversionApi(ClientConfig.baseUrl)
val rates = api.getCurrencyRates("usd")
val eurRate = rates["usd"]?.get("eur")
println("1 USD = $eurRate EUR")
```

### **Scenario 2: Check API Health**
```kotlin
val healthApi = HealthCheckApi(ClientConfig.baseUrl)
val health = healthApi.healthCheck()
if (health.status == "UP") {
    println("✅ API is healthy")
} else {
    println("❌ API is down")
}
```

### **Scenario 3: List All Supported Currencies**
```kotlin
val infoApi = CurrencyInformationApi(ClientConfig.baseUrl)
val response = infoApi.getSupportedCurrencies()
println("Supported currencies: ${response.supportedCurrencies}")
```

## 🚀 **Production Deployment**

### **Environment Variables for Production**
```bash
export CURRENCY_API_BASE_URL="https://api.yourdomain.com"
export CURRENCY_API_TIMEOUT=60000
export CURRENCY_API_CONNECTION_TIMEOUT=15000
export CURRENCY_API_MAX_CONNECTIONS=200
```

### **Build for Production**
```bash
./gradlew clean build -Pprofile=production
./gradlew jar
```

## 📞 **Support**

- **API Documentation**: `http://localhost:8080/swagger-ui`
- **OpenAPI Spec**: `http://localhost:8080/api-docs`
- **Health Check**: `http://localhost:8080/api/health`

---

**Generated with OpenAPI Generator v7.14.0** | **Package**: `com.example.currency.client`