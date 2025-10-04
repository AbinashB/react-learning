# CurrencyConversionApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getCurrencyRates**](CurrencyConversionApi.md#getCurrencyRates) | **GET** /api/{currencyCode} | Get currency conversion rates
[**getCurrencyRatesWithHttpInfo**](CurrencyConversionApi.md#getCurrencyRatesWithHttpInfo) | **GET** /api/{currencyCode} | Get currency conversion rates



## getCurrencyRates

> getCurrencyRates(getCurrencyRatesRequest): ApiRequest[Map[String, Map[String, Double]]]

Get currency conversion rates

Retrieves conversion rates for a specific base currency. Returns exchange rates from the base currency to all other supported currencies.

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
    val apiInstance = CurrencyConversionApi("http://localhost:8080")
    val currencyCode: String = usd // String | The base currency code (case-insensitive)
    
    val request = apiInstance.getCurrencyRates(currencyCode)
    val response = apiInvoker.execute(request)

    response.onComplete {
        case Success(ApiResponse(code, content, headers)) =>
            System.out.println(s"Status code: $code}")
            System.out.println(s"Response headers: ${headers.mkString(", ")}")
            System.out.println(s"Response body: $content")
        
        case Failure(error @ ApiError(code, message, responseContent, cause, headers)) =>
            System.err.println("Exception when calling CurrencyConversionApi#getCurrencyRates")
            System.err.println(s"Status code: $code}")
            System.err.println(s"Reason: $responseContent")
            System.err.println(s"Response headers: ${headers.mkString(", ")}")
            error.printStackTrace();

        case Failure(exception) => 
            System.err.println("Exception when calling CurrencyConversionApi#getCurrencyRates")
            exception.printStackTrace();
    }
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **currencyCode** | **String**| The base currency code (case-insensitive) |

### Return type

ApiRequest[**Map[String, Map[String, Double]]**]


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful response with currency conversion rates |  -  |
| **400** | Currency not supported |  -  |

