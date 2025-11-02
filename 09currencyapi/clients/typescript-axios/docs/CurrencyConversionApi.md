# CurrencyConversionApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getCurrencyRates**](#getcurrencyrates) | **GET** /api/{currencyCode} | Get currency conversion rates|

# **getCurrencyRates**
> { [key: string]: { [key: string]: number; }; } getCurrencyRates()

Retrieves conversion rates for a specific base currency. Returns exchange rates  from the base currency to all other supported currencies. 

### Example

```typescript
import {
    CurrencyConversionApi,
    Configuration
} from 'currency-converter-client';

const configuration = new Configuration();
const apiInstance = new CurrencyConversionApi(configuration);

let currencyCode: string; //The base currency code (case-insensitive) (default to undefined)

const { status, data } = await apiInstance.getCurrencyRates(
    currencyCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **currencyCode** | [**string**] | The base currency code (case-insensitive) | defaults to undefined|


### Return type

**{ [key: string]: { [key: string]: number; }; }**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Successful response with currency conversion rates |  -  |
|**400** | Currency not supported |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

