# Currency API - Kubernetes Deployment Guide

This directory contains Kubernetes manifests and scripts to deploy the Currency Converter API to a local Kubernetes cluster.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Manual Deployment](#manual-deployment)
- [Configuration](#configuration)
- [Autoscaling](#autoscaling)
- [Accessing the API](#accessing-the-api)
- [Monitoring](#monitoring)
- [Troubleshooting](#troubleshooting)
- [Cleanup](#cleanup)

## 🔧 Prerequisites

### Required Tools

1. **kubectl** - Kubernetes command-line tool
   ```bash
   # macOS
   brew install kubectl
   
   # Verify installation
   kubectl version --client
   ```

2. **Local Kubernetes Cluster** (choose one):
   - **Minikube** (Recommended for beginners)
     ```bash
     brew install minikube
     minikube start
     ```
   
   - **Kind** (Kubernetes in Docker)
     ```bash
     brew install kind
     kind create cluster
     ```
   
   - **Docker Desktop** with Kubernetes enabled
     - Open Docker Desktop → Settings → Kubernetes → Enable Kubernetes

### Required Addons

1. **Ingress Controller** (for external access)
   
   **For Minikube:**
   ```bash
   minikube addons enable ingress
   ```
   
   **For Kind:**
   ```bash
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
   ```
   
   **For Docker Desktop:**
   ```bash
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
   ```

2. **Metrics Server** (for autoscaling)
   
   **For Minikube:**
   ```bash
   minikube addons enable metrics-server
   ```
   
   **For Kind/Docker Desktop:**
   ```bash
   kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
   ```

## 🏗️ Architecture

### Components

```
┌─────────────────────────────────────────────────────┐
│                   Ingress                           │
│            (currency-api.local)                     │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                Service (ClusterIP)                  │
│         Load Balancer (Round-Robin)                 │
└────────────────────┬────────────────────────────────┘
                     │
         ┌───────────┴────────────┐
         ▼                        ▼
┌─────────────────┐      ┌─────────────────┐
│   Pod 1         │      │   Pod 2         │
│  currency-api   │ ...  │  currency-api   │
└─────────────────┘      └─────────────────┘
         ▲
         │
    ┌────┴────┐
    │   HPA   │
    │ (2-5)   │
    └─────────┘
```

### Kubernetes Resources

| Resource | File | Description |
|----------|------|-------------|
| **Deployment** | `deployment.yaml` | Manages 2-5 replicas of the Currency API |
| **Service** | `service.yaml` | Internal load balancer (ClusterIP) |
| **Ingress** | `ingress.yaml` | External access via HTTP |
| **HPA** | `hpa.yaml` | Auto-scales pods based on CPU/Memory |

### Key Features

- **Native Load Balancing**: Kubernetes Service distributes traffic (replaces NGINX)
- **Auto-scaling**: HPA scales from 2 to 5 replicas based on load
- **Health Checks**: Liveness and readiness probes ensure reliability
- **Resource Management**: CPU and memory limits prevent resource exhaustion

## 🚀 Quick Start

### 1. Build the Application

```bash
./k8s/build.sh
```

This script:
- Builds the JAR file using Gradle
- Creates Docker image (`currency-api:latest`)
- Loads image into your local cluster

### 2. Deploy to Kubernetes

```bash
./k8s/deploy.sh
```

This script:
- Checks prerequisites (Ingress, metrics-server)
- Applies all Kubernetes manifests
- Waits for pods to be ready
- Displays access information

### 3. Access the API

**For Minikube:**
```bash
# Get Minikube IP
minikube ip

# Add to /etc/hosts (replace <IP> with actual IP)
echo "<IP> currency-api.local" | sudo tee -a /etc/hosts

# Access the API
curl http://currency-api.local/
```

**For Kind/Docker Desktop:**
```bash
# Add to /etc/hosts
echo "127.0.0.1 currency-api.local" | sudo tee -a /etc/hosts

# Access the API
curl http://currency-api.local/
```

### 4. Test the API

```bash
# Health check
curl http://currency-api.local/

# Get supported currencies
curl http://currency-api.local/api/currencies

# Convert USD to other currencies
curl http://currency-api.local/api/usd
```

## 📝 Manual Deployment

If you prefer manual deployment:

```bash
# 1. Build JAR
./gradlew clean build

# 2. Build Docker image
docker build -t currency-api:latest .

# 3. Load into cluster (Minikube)
minikube image load currency-api:latest

# 4. Apply manifests
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/hpa.yaml

# 5. Wait for ready
kubectl wait --for=condition=available --timeout=180s deployment/currency-api
```

## ⚙️ Configuration

### Scaling Configuration

Edit `k8s/hpa.yaml` to adjust autoscaling parameters:

```yaml
spec:
  minReplicas: 2        # Minimum pods (baseline)
  maxReplicas: 5        # Maximum pods
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        averageUtilization: 70  # Scale at 70% CPU
```

### Resource Limits

Edit `k8s/deployment.yaml` to adjust resource allocation:

```yaml
resources:
  requests:
    cpu: 100m       # Minimum CPU
    memory: 256Mi   # Minimum Memory
  limits:
    cpu: 500m       # Maximum CPU
    memory: 512Mi   # Maximum Memory
```

### Environment Variables

Add custom environment variables in `k8s/deployment.yaml`:

```yaml
env:
- name: INSTANCE_NAME
  valueFrom:
    fieldRef:
      fieldPath: metadata.name
- name: CUSTOM_VAR
  value: "custom_value"
```

## 📊 Autoscaling

The Currency API uses **HorizontalPodAutoscaler (HPA)** for dynamic scaling.

### How It Works

- **Baseline**: Starts with 2 replicas
- **Scale Up**: When average CPU > 70% or Memory > 80%
- **Scale Down**: After 5 minutes of low usage (prevents flapping)
- **Maximum**: Scales up to 5 replicas

### Monitor HPA

```bash
# Watch HPA status
kubectl get hpa currency-api-hpa --watch

# Detailed HPA information
kubectl describe hpa currency-api-hpa

# View current metrics
kubectl top pods -l app=currency-api
```

### Manual Scaling

To temporarily override HPA:

```bash
# Scale to specific number
kubectl scale deployment currency-api --replicas=3

# Delete HPA to prevent auto-scaling
kubectl delete hpa currency-api-hpa
```

### Load Testing

Test autoscaling behavior:

```bash
# Install hey (HTTP load generator)
brew install hey

# Generate load
hey -z 60s -c 50 http://currency-api.local/api/usd

# Watch pods scale up
kubectl get pods -l app=currency-api --watch
```

## 🌐 Accessing the API

### Local Access (within cluster)

```bash
# Port-forward to access directly
kubectl port-forward service/currency-api 8080:80

# Access via localhost
curl http://localhost:8080/
```

### External Access (via Ingress)

**Setup hosts file:**
```bash
# Minikube
echo "$(minikube ip) currency-api.local" | sudo tee -a /etc/hosts

# Kind/Docker Desktop
echo "127.0.0.1 currency-api.local" | sudo tee -a /etc/hosts
```

**Access endpoints:**
```bash
# Root (health check)
curl http://currency-api.local/

# API endpoints
curl http://currency-api.local/api/health
curl http://currency-api.local/api/currencies
curl http://currency-api.local/api/usd
curl http://currency-api.local/api/eur
```

### Using NodePort (alternative)

If Ingress doesn't work, use NodePort:

```bash
# Edit service to use NodePort
kubectl patch service currency-api -p '{"spec":{"type":"NodePort"}}'

# Get the port
kubectl get service currency-api

# Access (Minikube)
minikube service currency-api --url
```

## 📈 Monitoring

### View Logs

```bash
# All pods
kubectl logs -f -l app=currency-api

# Specific pod
kubectl logs -f <pod-name>

# Previous pod (if crashed)
kubectl logs -f <pod-name> --previous
```

### Check Pod Status

```bash
# List pods
kubectl get pods -l app=currency-api

# Detailed pod info
kubectl describe pod <pod-name>

# Pod events
kubectl get events --sort-by='.lastTimestamp'
```

### Check Service/Ingress

```bash
# Service details
kubectl get service currency-api
kubectl describe service currency-api

# Ingress details
kubectl get ingress currency-api-ingress
kubectl describe ingress currency-api-ingress
```

### Resource Usage

```bash
# Pod resource usage
kubectl top pods -l app=currency-api

# Node resource usage
kubectl top nodes
```

## 🔧 Troubleshooting

### Pods Not Starting

**Check pod status:**
```bash
kubectl get pods -l app=currency-api
kubectl describe pod <pod-name>
```

**Common issues:**
- **ImagePullBackOff**: Image not loaded into cluster
  ```bash
  # Minikube
  minikube image load currency-api:latest
  
  # Kind
  kind load docker-image currency-api:latest
  ```
- **CrashLoopBackOff**: Application failing to start
  ```bash
  kubectl logs <pod-name>
  ```

### Ingress Not Working

**Check Ingress controller:**
```bash
kubectl get pods -n ingress-nginx
```

**If not installed:**
```bash
# Minikube
minikube addons enable ingress

# Kind
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

**Check Ingress status:**
```bash
kubectl describe ingress currency-api-ingress
```

### HPA Not Scaling

**Check metrics-server:**
```bash
kubectl get deployment metrics-server -n kube-system
```

**If not installed:**
```bash
# Minikube
minikube addons enable metrics-server

# Others
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

**Check HPA status:**
```bash
kubectl describe hpa currency-api-hpa
kubectl top pods -l app=currency-api
```

### Cannot Connect to Cluster

```bash
# Check cluster status (Minikube)
minikube status

# Check cluster info
kubectl cluster-info

# Check context
kubectl config current-context
```

### Pods Stuck Terminating

```bash
# Force delete
kubectl delete pod <pod-name> --grace-period=0 --force
```

## 🧹 Cleanup

### Using Script

```bash
./k8s/delete.sh
```

### Manual Cleanup

```bash
# Delete all resources
kubectl delete -f k8s/

# Or delete individually
kubectl delete deployment currency-api
kubectl delete service currency-api
kubectl delete ingress currency-api-ingress
kubectl delete hpa currency-api-hpa
```

### Complete Cleanup (including cluster)

```bash
# Minikube
minikube delete

# Kind
kind delete cluster

# Docker Desktop
# Disable Kubernetes in Docker Desktop settings
```

## 📚 Additional Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [HorizontalPodAutoscaler](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/)
- [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)
- [Minikube Guide](https://minikube.sigs.k8s.io/docs/)
- [Kind Guide](https://kind.sigs.k8s.io/)

## 🆚 Comparison with Docker Compose

| Feature | Docker Compose | Kubernetes |
|---------|----------------|------------|
| Load Balancer | NGINX container | Service (built-in) |
| Replicas | 2 static instances | 2-5 dynamic (HPA) |
| Scaling | Manual restart | Automatic |
| Health Checks | Basic restart | Liveness/Readiness |
| Resource Limits | None | CPU/Memory limits |
| External Access | Port mapping | Ingress |
| Service Discovery | Container names | DNS |

## 💡 Tips

1. **Development**: Use `imagePullPolicy: Never` to avoid pulling from registry
2. **Debugging**: Use `kubectl exec -it <pod> -- /bin/sh` to access pod shell
3. **Quick Restart**: `kubectl rollout restart deployment/currency-api`
4. **View All Resources**: `kubectl get all -l app=currency-api`
5. **Export YAML**: `kubectl get deployment currency-api -o yaml > deployment-backup.yaml`

