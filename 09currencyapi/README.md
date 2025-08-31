# Currency Converter API

A simple REST API built with Kotlin and Spring Boot that provides currency conversion rates.

## Features

- Get conversion rates for a specific base currency
- Returns rates in the format: `{currencycode: {inr: 1, cny: 2, etc}}`
- Support for major currencies (USD, EUR, GBP, INR, JPY, CNY, CAD, AUD, CHF, SGD, KRW)
- Health check endpoint
- List supported currencies

## API Endpoints

### Get Currency Conversion Rates
```
GET /api/{currencyCode}
```

**Example:**
```bash
curl http://localhost:8080/api/usd
```

**Response:**
```json
{
  "usd": {
    "inr": 83.25,
    "eur": 0.92,
    "gbp": 0.79,
    "jpy": 149.5,
    "cny": 7.24,
    "cad": 1.36,
    "aud": 1.53,
    "chf": 0.88,
    "sgd": 1.35,
    "krw": 1320.5
  }
}
```

### Get Supported Currencies
```
GET /api/currencies
```

**Response:**
```json
{
  "supportedCurrencies": ["usd", "eur", "gbp", "inr", "jpy", "cny"],
  "count": 6
}
```

### Health Check
```
GET /api/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Currency Converter API"
}
```

## Running the Application

1. Make sure you have Java 17+ installed
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```
3. The API will be available at `http://localhost:8080`

## Building the Application

```bash
./gradlew build
```

## Supported Currencies

- USD (US Dollar)
- EUR (Euro)
- GBP (British Pound)
- INR (Indian Rupee)
- JPY (Japanese Yen)
- CNY (Chinese Yuan)
- CAD (Canadian Dollar)
- AUD (Australian Dollar)
- CHF (Swiss Franc)
- SGD (Singapore Dollar)
- KRW (South Korean Won)

## Note

This API uses mock exchange rates for demonstration purposes. In a production environment, you would integrate with a real currency exchange rate API like Fixer.io, CurrencyLayer, or similar services.
