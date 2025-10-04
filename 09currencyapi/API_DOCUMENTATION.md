# Currency Converter API - OpenAPI Documentation

This directory contains the OpenAPI specification for the Currency Converter API, which can be used by other clients to consume the API.

## Files

- `openapi.yaml` - OpenAPI 3.0 specification in YAML format
- `openapi.json` - OpenAPI 3.0 specification in JSON format
- `API_DOCUMENTATION.md` - This documentation file

## How to Use the OpenAPI Specification

### 1. API Documentation Viewers

You can view the interactive API documentation using various tools:

#### Swagger UI (Online)
1. Go to [Swagger Editor](https://editor.swagger.io/)
2. Copy the contents of `openapi.yaml` or `openapi.json`
3. Paste it into the editor to see the interactive documentation

#### Local Swagger UI
```bash
# Using Docker
docker run -p 8081:8080 -e SWAGGER_JSON=/openapi.json -v $(pwd)/openapi.json:/openapi.json swaggerapi/swagger-ui

# Then visit http://localhost:8081
```

### 2. Code Generation

Generate client SDKs in various programming languages:

#### Using OpenAPI Generator CLI
```bash
# Install OpenAPI Generator
npm install @openapitools/openapi-generator-cli -g

# Generate JavaScript/TypeScript client
openapi-generator-cli generate -i openapi.yaml -g typescript-fetch -o ./clients/typescript

# Generate Python client
openapi-generator-cli generate -i openapi.yaml -g python -o ./clients/python

# Generate Java client
openapi-generator-cli generate -i openapi.yaml -g java -o ./clients/java

# Generate Go client
openapi-generator-cli generate -i openapi.yaml -g go -o ./clients/go
```

#### Using Swagger Codegen
```bash
# Generate clients using Swagger Codegen
swagger-codegen generate -i openapi.yaml -l javascript -o ./clients/javascript
swagger-codegen generate -i openapi.yaml -l python -o ./clients/python
swagger-codegen generate -i openapi.yaml -l java -o ./clients/java
```

### 3. API Testing

#### Using curl with the specification
```bash
# Get USD conversion rates
curl -X GET "http://localhost:8080/api/usd" -H "accept: application/json"

# Get supported currencies
curl -X GET "http://localhost:8080/api/currencies" -H "accept: application/json"

# Health check
curl -X GET "http://localhost:8080/api/health" -H "accept: application/json"
```

#### Using Postman
1. Import the `openapi.json` file into Postman
2. Postman will automatically create a collection with all endpoints
3. Set the base URL to your server (e.g., `http://localhost:8080`)

#### Using Insomnia
1. Import the `openapi.yaml` or `openapi.json` file
2. All endpoints will be available for testing

### 4. Integration Examples

#### JavaScript/TypeScript (Fetch API)
```javascript
// Get currency rates
async function getCurrencyRates(currencyCode) {
  const response = await fetch(`http://localhost:8080/api/${currencyCode}`);
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return await response.json();
}

// Get supported currencies
async function getSupportedCurrencies() {
  const response = await fetch('http://localhost:8080/api/currencies');
  return await response.json();
}

// Usage
getCurrencyRates('usd').then(data => console.log(data));
```

#### Python (requests)
```python
import requests

class CurrencyAPI:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
    
    def get_currency_rates(self, currency_code):
        response = requests.get(f"{self.base_url}/api/{currency_code}")
        response.raise_for_status()
        return response.json()
    
    def get_supported_currencies(self):
        response = requests.get(f"{self.base_url}/api/currencies")
        response.raise_for_status()
        return response.json()
    
    def health_check(self):
        response = requests.get(f"{self.base_url}/api/health")
        response.raise_for_status()
        return response.json()

# Usage
api = CurrencyAPI()
usd_rates = api.get_currency_rates('usd')
print(usd_rates)
```

#### Java (using OkHttp)
```java
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyAPIClient {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;
    
    public CurrencyAPIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public Map<String, Object> getCurrencyRates(String currencyCode) throws Exception {
        Request request = new Request.Builder()
            .url(baseUrl + "/api/" + currencyCode)
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return mapper.readValue(response.body().string(), Map.class);
        }
    }
}
```

## API Endpoints Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/{currencyCode}` | GET | Get conversion rates for a specific currency |
| `/api/currencies` | GET | Get list of supported currencies |
| `/api/health` | GET | Health check endpoint |

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

## Response Format

### Success Response (Currency Rates)
```json
{
  "usd": {
    "usd": 1.0,
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

### Error Response
```json
{
  "error": "Currency not supported",
  "message": "The currency code 'xyz' is not supported",
  "supportedCurrencies": ["usd", "eur", "gbp", "inr", "jpy", "cny", "cad", "aud", "chf", "sgd", "krw"]
}
```

## Validation

The OpenAPI specification includes validation rules:
- Currency codes must be exactly 3 characters (letters only)
- All currency codes are case-insensitive (converted to lowercase in responses)
- Invalid currency codes return a 400 Bad Request with error details

## Notes

- This API uses mock exchange rates for demonstration purposes
- All currency codes in responses are lowercase
- The API is stateless and doesn't require authentication
- CORS is not explicitly configured in the specification but may be needed for web clients
