# ErrorResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**error** | **string** | Error type | [default to undefined]
**message** | **string** | Detailed error message | [default to undefined]
**supportedCurrencies** | **Array&lt;string&gt;** | List of supported currency codes (present in currency-related errors) | [optional] [default to undefined]

## Example

```typescript
import { ErrorResponse } from 'currency-converter-client';

const instance: ErrorResponse = {
    error,
    message,
    supportedCurrencies,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
