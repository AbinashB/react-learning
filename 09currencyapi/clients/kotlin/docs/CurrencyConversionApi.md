# CurrencyConversionApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getCurrencyRates**](CurrencyConversionApi.md#getCurrencyRates) | **GET** /api/{currencyCode} | Get currency conversion rates |


<a id="getCurrencyRates"></a>
# **getCurrencyRates**
> kotlin.collections.Map&lt;kotlin.String, kotlin.collections.Map&lt;kotlin.String, kotlin.Double&gt;&gt; getCurrencyRates(currencyCode)

Get currency conversion rates

Retrieves conversion rates for a specific base currency. Returns exchange rates from the base currency to all other supported currencies.

### Example
```kotlin
// Import classes:
//import com.example.currency.client.infrastructure.*
//import com.example.currency.client.models.*

val apiInstance = CurrencyConversionApi()
val currencyCode : kotlin.String = usd // kotlin.String | The base currency code (case-insensitive)
try {
    val result : kotlin.collections.Map<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Double>> = apiInstance.getCurrencyRates(currencyCode)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CurrencyConversionApi#getCurrencyRates")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CurrencyConversionApi#getCurrencyRates")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **currencyCode** | **kotlin.String**| The base currency code (case-insensitive) | |

### Return type

**kotlin.collections.Map&lt;kotlin.String, kotlin.collections.Map&lt;kotlin.String, kotlin.Double&gt;&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

