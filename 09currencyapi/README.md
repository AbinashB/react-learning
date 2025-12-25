# Currency Converter API

A simple REST API built with vanilla Kotlin and Ktor that provides currency conversion rates.

## Features

- Get conversion rates for a specific base currency
- Returns rates in the format: `{currencycode: {inr: 1, cny: 2, etc}}`
- Support for major currencies (USD, EUR, GBP, INR, JPY, CNY, CAD, AUD, CHF, SGD, KRW)
- Health check endpoint
- List supported currencies

## API Endpoints

### Get Currency Conversion Rates
```
GET /api/{currencyCode}
```

**Example:**
```bash
curl http://localhost:8080/api/usd
```

**Response:**
```json
{
  "usd": {
    "inr": 83.25,
    "eur": 0.92,
    "gbp": 0.79,
    "jpy": 149.5,
    "cny": 7.24,
    "cad": 1.36,
    "aud": 1.53,
    "chf": 0.88,
    "sgd": 1.35,
    "krw": 1320.5
  }
}
```

### Get Supported Currencies
```
GET /api/currencies
```

**Response:**
```json
{
  "supportedCurrencies": ["usd", "eur", "gbp", "inr", "jpy", "cny"],
  "count": 6
}
```

### Health Check
```
GET /api/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Currency Converter API"
}
```

## Running the Application

### Option 1: Local Development
1. Make sure you have Java 17+ installed
2. Run the application:
   ```bash
   ./gradlew run
   ```
3. The API will be available at `http://localhost:8080`

### Option 2: Docker Compose (Recommended for testing)
Run with load balancing (2 instances + NGINX):
```bash
docker-compose up --build
```
Access at `http://localhost:8080`

### Option 3: Kubernetes (Recommended for production-like setup)
Deploy to local Kubernetes cluster with autoscaling:
```bash
# Build and load into cluster
./k8s/build.sh

# Deploy to Kubernetes
./k8s/deploy.sh
```
See [k8s/README.md](k8s/README.md) for detailed instructions.

**Why Kubernetes?**
- **Auto-scaling**: 2-5 replicas based on CPU/Memory load
- **Self-healing**: Automatic pod restarts and health checks
- **Native load balancing**: No need for separate NGINX container
- **Production-ready**: Industry-standard orchestration

## Building the Application

```bash
./gradlew build
```

## Supported Currencies

- USD (US Dollar)
- EUR (Euro)
- GBP (British Pound)
- INR (Indian Rupee)
- JPY (Japanese Yen)
- CNY (Chinese Yuan)
- CAD (Canadian Dollar)
- AUD (Australian Dollar)
- CHF (Swiss Franc)
- SGD (Singapore Dollar)
- KRW (South Korean Won)

## Technology Stack

- **Kotlin** - Programming language
- **Ktor** - Lightweight web framework for Kotlin
- **Kotlinx Serialization** - JSON serialization
- **Netty** - HTTP server engine

## Kubernetes Deployment

This project includes full Kubernetes support with autoscaling capabilities.

### Quick Start

```bash
# 1. Build and load image into cluster
./k8s/build.sh

# 2. Deploy to Kubernetes
./k8s/deploy.sh

# 3. Access the API
curl http://currency-api.local/
```

### Features

- **HorizontalPodAutoscaler (HPA)**: Automatically scales from 2 to 5 replicas based on CPU/Memory usage
- **Health Checks**: Liveness and readiness probes ensure high availability
- **Resource Management**: CPU and memory limits prevent resource exhaustion
- **Ingress**: External access via HTTP with NGINX Ingress Controller
- **Service**: Native Kubernetes load balancing (replaces Docker Compose NGINX)

### Architecture Comparison

| Feature | Docker Compose | Kubernetes |
|---------|----------------|------------|
| Load Balancer | NGINX container | Service (built-in) |
| Replicas | 2 static | 2-5 dynamic (HPA) |
| Scaling | Manual restart | Automatic |
| Health Checks | Basic restart | Liveness/Readiness |
| Resource Limits | None | CPU/Memory managed |
| External Access | Port mapping | Ingress |

### Prerequisites

- kubectl installed
- Local Kubernetes cluster (Minikube, Kind, or Docker Desktop)
- Ingress controller enabled
- Metrics server enabled (for autoscaling)

**For detailed instructions, troubleshooting, and configuration options, see [k8s/README.md](k8s/README.md)**

### Monitoring Autoscaling

```bash
# Watch HPA status
kubectl get hpa currency-api-hpa --watch

# View pod resource usage
kubectl top pods -l app=currency-api

# Generate load to trigger scaling
hey -z 60s -c 50 http://currency-api.local/api/usd
```

### Cleanup

```bash
./k8s/delete.sh
```

## Note

This API uses mock exchange rates for demonstration purposes. In a production environment, you would integrate with a real currency exchange rate API like Fixer.io, CurrencyLayer, or similar services.
