# CurrencyInformationApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getSupportedCurrencies**](CurrencyInformationApi.md#getSupportedCurrencies) | **GET** /api/currencies | Get supported currencies
[**getSupportedCurrenciesWithHttpInfo**](CurrencyInformationApi.md#getSupportedCurrenciesWithHttpInfo) | **GET** /api/currencies | Get supported currencies



## getSupportedCurrencies

> getSupportedCurrencies(): ApiRequest[SupportedCurrenciesResponse]

Get supported currencies

Returns a list of all supported currency codes along with the count

### Example

```scala
// Import classes:
import 
import org.openapitools.client.core._
import org.openapitools.client.core.CollectionFormats._
import org.openapitools.client.core.ApiKeyLocations._

import akka.actor.ActorSystem
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Example extends App {
    
    implicit val system: ActorSystem = ActorSystem()
    import system.dispatcher

    val apiInvoker = ApiInvoker()
    val apiInstance = CurrencyInformationApi("http://localhost:8080")    
    val request = apiInstance.getSupportedCurrencies()
    val response = apiInvoker.execute(request)

    response.onComplete {
        case Success(ApiResponse(code, content, headers)) =>
            System.out.println(s"Status code: $code}")
            System.out.println(s"Response headers: ${headers.mkString(", ")}")
            System.out.println(s"Response body: $content")
        
        case Failure(error @ ApiError(code, message, responseContent, cause, headers)) =>
            System.err.println("Exception when calling CurrencyInformationApi#getSupportedCurrencies")
            System.err.println(s"Status code: $code}")
            System.err.println(s"Reason: $responseContent")
            System.err.println(s"Response headers: ${headers.mkString(", ")}")
            error.printStackTrace();

        case Failure(exception) => 
            System.err.println("Exception when calling CurrencyInformationApi#getSupportedCurrencies")
            exception.printStackTrace();
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

ApiRequest[[**SupportedCurrenciesResponse**](SupportedCurrenciesResponse.md)]


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful response with supported currencies |  -  |

