# CurrencyConverter.Client.Api.CurrencyConversionApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|--------|--------------|-------------|
| [**GetCurrencyRates**](CurrencyConversionApi.md#getcurrencyrates) | **GET** /api/{currencyCode} | Get currency conversion rates |

<a id="getcurrencyrates"></a>
# **GetCurrencyRates**
> Dictionary&lt;string, Dictionary&lt;string, double&gt;&gt; GetCurrencyRates (string currencyCode)

Get currency conversion rates

Retrieves conversion rates for a specific base currency. Returns exchange rates from the base currency to all other supported currencies.


### Parameters

| Name | Type | Description | Notes |
|------|------|-------------|-------|
| **currencyCode** | **string** | The base currency code (case-insensitive) |  |

### Return type

**Dictionary<string, Dictionary<string, double>>**

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

[[Back to top]](#) [[Back to API list]](../../README.md#documentation-for-api-endpoints) [[Back to Model list]](../../README.md#documentation-for-models) [[Back to README]](../../README.md)

