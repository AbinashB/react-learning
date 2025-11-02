# Currency Converter Client SDK Integration

This document describes how the Currency Converter app has been integrated with the auto-generated TypeScript-Axios client SDK from the Currency Converter API.

## Overview

The app now uses a professionally generated SDK instead of manual fetch calls, providing:
- ✅ Type safety with TypeScript
- ✅ Auto-generated from OpenAPI specification
- ✅ Built-in error handling with Axios
- ✅ Consistent API across the codebase
- ✅ Easy maintenance and updates

## SDK Location

The SDK is located at:
```
../09currencyapi/clients/typescript-axios
```

It's installed as a local npm package using:
```json
"currency-converter-client": "file:../09currencyapi/clients/typescript-axios"
```

## Implementation Details

### Custom Hook: `useCurrencyInfo.js`

The custom hook has been refactored to use the SDK:

**Before (using fetch):**
```javascript
fetch(`http://localhost:8080/api/${currency}`)
  .then((res) => res.json())
  .then((res) => setData(res[currency]))
```

**After (using SDK):**
```javascript
import { CurrencyConversionApi, Configuration } from 'currency-converter-client'

const configuration = new Configuration({
  basePath: 'http://localhost:8080'
})
const api = new CurrencyConversionApi(configuration)

api.getCurrencyRates(currency)
  .then((response) => {
    const currencyData = response.data[currency]
    setData(currencyData || {})
  })
  .catch((error) => {
    console.error(`Error fetching currency rates:`, error)
    setData({})
  })
```

### Key Benefits

1. **Type Safety**: The SDK provides TypeScript types for all API responses
2. **Error Handling**: Built-in Axios error handling with proper error types
3. **Configuration**: Centralized API configuration (base URL, timeouts, etc.)
4. **Maintainability**: When the API changes, regenerate the SDK and get updated types automatically

## API Endpoints Available

The SDK provides access to all API endpoints:

| API Class | Method | Description |
|-----------|--------|-------------|
| `CurrencyConversionApi` | `getCurrencyRates(currencyCode)` | Get conversion rates for a currency |
| `CurrencyInformationApi` | `getSupportedCurrencies()` | Get list of supported currencies |
| `HealthCheckApi` | `healthCheck()` | Check API health status |

## Regenerating the SDK

If the API specification changes, regenerate the SDK:

```bash
cd ../09currencyapi
npx @openapitools/openapi-generator-cli generate \
  -i openapi.yaml \
  -g typescript-axios \
  -o clients/typescript-axios \
  --additional-properties=npmName=currency-converter-client,npmVersion=1.0.0,supportsES6=true

cd clients/typescript-axios
npm install
npm run build
```

Then reinstall in the React app:
```bash
cd ../../08currencyconverter
npm install
```

## Testing

1. **Start the API Server:**
   ```bash
   cd ../09currencyapi
   ./gradlew run
   ```

2. **Verify API is running:**
   ```bash
   curl http://localhost:8080/api/health
   # Should return: {"status":"UP","service":"Currency Converter API"}
   ```

3. **Start the React App:**
   ```bash
   npm run dev
   ```

4. **Test the currency converter:**
   - Open http://localhost:5173
   - Select currencies and enter amounts
   - Verify conversions work correctly

## Configuration

The SDK is configured with:
- **Base URL**: `http://localhost:8080` (can be changed in `useCurrencyInfo.js`)
- **HTTP Client**: Axios
- **Response Type**: JSON

To change the base URL for production:
```javascript
const configuration = new Configuration({
  basePath: 'https://api.production.com'
})
```

## Dependencies

- `axios`: ^1.6.1 (HTTP client)
- `currency-converter-client`: file:../09currencyapi/clients/typescript-axios (SDK)

## Bug Fixes

During integration, fixed a bug in `App.jsx`:
- Line 15: Changed `setForm(from)` to `setTo(from)` in the swap function

## Future Enhancements

Potential improvements:
1. Add environment variables for API base URL
2. Implement request/response interceptors for logging
3. Add retry logic for failed requests
4. Cache currency rates to reduce API calls
5. Add loading states and better error UI

---

**Generated**: November 2, 2025  
**SDK Version**: 1.0.0  
**OpenAPI Generator Version**: 7.14.0

