# HealthCheckApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**healthCheck**](HealthCheckApi.md#healthCheck) | **GET** /api/health | Health check endpoint |


<a id="healthCheck"></a>
# **healthCheck**
> HealthResponse healthCheck()

Health check endpoint

Returns the health status of the API service

### Example
```kotlin
// Import classes:
//import com.example.currency.client.infrastructure.*
//import com.example.currency.client.models.*

val apiInstance = HealthCheckApi()
try {
    val result : HealthResponse = apiInstance.healthCheck()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling HealthCheckApi#healthCheck")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling HealthCheckApi#healthCheck")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**HealthResponse**](HealthResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

