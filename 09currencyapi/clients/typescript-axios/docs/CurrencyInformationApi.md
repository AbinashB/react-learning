# CurrencyInformationApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getSupportedCurrencies**](#getsupportedcurrencies) | **GET** /api/currencies | Get supported currencies|

# **getSupportedCurrencies**
> SupportedCurrenciesResponse getSupportedCurrencies()

Returns a list of all supported currency codes along with the count

### Example

```typescript
import {
    CurrencyInformationApi,
    Configuration
} from 'currency-converter-client';

const configuration = new Configuration();
const apiInstance = new CurrencyInformationApi(configuration);

const { status, data } = await apiInstance.getSupportedCurrencies();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SupportedCurrenciesResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Successful response with supported currencies |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

