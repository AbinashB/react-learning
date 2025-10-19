// C# .NET Core Usage Example for Currency Converter API Client
// Using Configuration

using System;
using System.Threading.Tasks;
using CurrencyConverter.Client.Api;
using CurrencyConverter.Client.Client;
using CurrencyConverter.Client.Model;
using CurrencyConverter.Client.Config;

namespace CurrencyConverter.Example
{
    class Program
    {
        static async Task Main(string[] args)
        {
            Console.WriteLine("=== Currency Converter API Client - .NET Core Example ===");
            
            // Load and print configuration for debugging
            var clientConfig = ClientConfig.Instance;
            clientConfig.PrintConfig();
            
            // Validate configuration
            if (!clientConfig.ValidateConfig())
            {
                Console.WriteLine("Configuration validation failed. Exiting...");
                return;
            }
            
            // Configure the API client using configuration
            var basePath = clientConfig.BaseUrl;
            var configuration = new Configuration
            {
                BasePath = basePath,
                Timeout = clientConfig.HttpTimeoutSeconds * 1000 // Convert to milliseconds
            };
            
            // Initialize API clients
            var currencyConversionApi = new CurrencyConversionApi(configuration);
            var currencyInformationApi = new CurrencyInformationApi(configuration);
            var healthCheckApi = new HealthCheckApi(configuration);
            
            try
            {
                // Example 1: Health check
                Console.WriteLine("\n1. Health Check:");
                var healthResponse = await healthCheckApi.HealthCheckAsync();
                Console.WriteLine($"   Status: {healthResponse.Status}");
                Console.WriteLine($"   Service: {healthResponse.Service}");
                
                // Example 2: Get supported currencies
                Console.WriteLine("\n2. Supported Currencies:");
                var supportedCurrencies = await currencyInformationApi.GetSupportedCurrenciesAsync();
                Console.WriteLine($"   Count: {supportedCurrencies.Count}");
                Console.WriteLine($"   Currencies: {string.Join(", ", supportedCurrencies.SupportedCurrencies)}");
                
                // Example 3: Get USD conversion rates
                Console.WriteLine("\n3. USD Conversion Rates:");
                var usdRates = await currencyConversionApi.GetCurrencyRatesAsync("usd");
                foreach (var currencyPair in usdRates)
                {
                    Console.WriteLine($"   {currencyPair.Key}:");
                    foreach (var rate in currencyPair.Value)
                    {
                        Console.WriteLine($"     {rate.Key}: {rate.Value}");
                    }
                }
                
                // Example 4: Get EUR conversion rates
                Console.WriteLine("\n4. EUR Conversion Rates:");
                var eurRates = await currencyConversionApi.GetCurrencyRatesAsync("eur");
                foreach (var currencyPair in eurRates)
                {
                    Console.WriteLine($"   {currencyPair.Key}:");
                    foreach (var rate in currencyPair.Value)
                    {
                        Console.WriteLine($"     {rate.Key}: {rate.Value}");
                    }
                }
                
                // Example 5: Error handling - invalid currency
                Console.WriteLine("\n5. Error Handling (Invalid Currency):");
                try
                {
                    await currencyConversionApi.GetCurrencyRatesAsync("xyz");
                }
                catch (ApiException ex)
                {
                    Console.WriteLine($"   Expected error for invalid currency:");
                    Console.WriteLine($"   Status Code: {ex.ErrorCode}");
                    Console.WriteLine($"   Message: {ex.Message}");
                }
                
                // Example 6: Using different currencies
                Console.WriteLine("\n6. Multiple Currency Examples:");
                var currencies = new[] { "gbp", "inr", "jpy" };
                
                foreach (var currency in currencies)
                {
                    try
                    {
                        var rates = await currencyConversionApi.GetCurrencyRatesAsync(currency);
                        Console.WriteLine($"   {currency.ToUpper()} rates available: {rates.Count > 0}");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"   Error getting {currency} rates: {ex.Message}");
                    }
                }
                
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Unexpected error: {ex.Message}");
                Console.WriteLine($"Stack trace: {ex.StackTrace}");
            }
            
            Console.WriteLine("\nPress any key to exit...");
            Console.ReadKey();
        }
    }
}

/*
To use this client:

1. Create a new .NET project:
dotnet new console -n CurrencyConverterExample
cd CurrencyConverterExample

2. Add the generated client as a project reference or NuGet package

3. Add required dependencies to .csproj:
<PackageReference Include="Newtonsoft.Json" Version="13.0.3" />
<PackageReference Include="System.ComponentModel.Annotations" Version="5.0.0" />

4. Build and run:
dotnet build
dotnet run

5. Or use in your existing project:
var api = new CurrencyConversionApi("http://localhost:8080");
var rates = await api.GetCurrencyRatesAsync("usd");
*/
