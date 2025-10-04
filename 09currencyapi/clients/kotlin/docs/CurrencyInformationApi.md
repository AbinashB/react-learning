# CurrencyInformationApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getSupportedCurrencies**](CurrencyInformationApi.md#getSupportedCurrencies) | **GET** /api/currencies | Get supported currencies |


<a id="getSupportedCurrencies"></a>
# **getSupportedCurrencies**
> SupportedCurrenciesResponse getSupportedCurrencies()

Get supported currencies

Returns a list of all supported currency codes along with the count

### Example
```kotlin
// Import classes:
//import com.example.currency.client.infrastructure.*
//import com.example.currency.client.models.*

val apiInstance = CurrencyInformationApi()
try {
    val result : SupportedCurrenciesResponse = apiInstance.getSupportedCurrencies()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CurrencyInformationApi#getSupportedCurrencies")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CurrencyInformationApi#getSupportedCurrencies")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SupportedCurrenciesResponse**](SupportedCurrenciesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

