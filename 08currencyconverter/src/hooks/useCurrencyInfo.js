import { useEffect, useState } from "react"
import { CurrencyConversionApi, Configuration } from 'currency-converter-client'

function useCurrencyInfo(currency){
  const [data, setData] = useState({})
  
  useEffect(() => {
    // Initialize the API client with configuration
    const configuration = new Configuration({
      basePath: 'http://localhost:8080'
    })
    const api = new CurrencyConversionApi(configuration)
    
    // Fetch currency rates using the SDK
    api.getCurrencyRates(currency)
      .then((response) => {
        // Extract the currency data from the response
        // Response format: { usd: { inr: 83.25, eur: 0.92, ... } }
        const currencyData = response.data[currency]
        setData(currencyData || {})
      })
      .catch((error) => {
        console.error(`Error fetching currency rates for ${currency}:`, error)
        setData({})
      })
  }, [currency])

  return data
}

export default useCurrencyInfo

