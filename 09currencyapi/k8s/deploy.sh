#!/bin/bash

# Deploy script for Currency API - Kubernetes deployment
# This script applies all Kubernetes manifests and verifies the deployment

set -e  # Exit on any error

echo "=========================================="
echo "Deploying Currency API to Kubernetes"
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
K8S_DIR="$(cd "$(dirname "$0")" && pwd)"
NAMESPACE="default"

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}Error: kubectl is not installed or not in PATH${NC}"
    exit 1
fi

# Check if cluster is accessible
if ! kubectl cluster-info &> /dev/null; then
    echo -e "${RED}Error: Cannot connect to Kubernetes cluster${NC}"
    echo "Please ensure your cluster is running:"
    echo "  - Minikube: minikube start"
    echo "  - Kind: kind create cluster"
    echo "  - Docker Desktop: Enable Kubernetes in settings"
    exit 1
fi

echo -e "${GREEN}✓ Connected to Kubernetes cluster${NC}"
echo ""

# Check if metrics-server is installed (required for HPA)
echo -e "${BLUE}Checking for metrics-server (required for autoscaling)...${NC}"
if ! kubectl get deployment metrics-server -n kube-system &> /dev/null; then
    echo -e "${YELLOW}Warning: metrics-server not found${NC}"
    echo ""
    echo "HPA (HorizontalPodAutoscaler) requires metrics-server to function."
    echo "To enable it:"
    
    # Detect cluster type and provide appropriate command
    if command -v minikube &> /dev/null && minikube status &> /dev/null; then
        echo -e "${BLUE}  For Minikube:${NC} minikube addons enable metrics-server"
    elif command -v kind &> /dev/null && kind get clusters &> /dev/null 2>&1; then
        echo -e "${BLUE}  For Kind:${NC} kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml"
    else
        echo -e "${BLUE}  General:${NC} kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml"
    fi
    
    echo ""
    read -p "Do you want to continue without metrics-server? (HPA won't work) [y/N]: " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    echo -e "${GREEN}✓ metrics-server is installed${NC}"
fi

echo ""

# Check if Ingress controller is installed
echo -e "${BLUE}Checking for Ingress controller...${NC}"
if ! kubectl get pods -n ingress-nginx &> /dev/null && \
   ! kubectl get pods -A | grep -q ingress; then
    echo -e "${YELLOW}Warning: No Ingress controller found${NC}"
    echo ""
    echo "Ingress requires an Ingress controller to function."
    echo "To enable it:"
    
    # Detect cluster type and provide appropriate command
    if command -v minikube &> /dev/null && minikube status &> /dev/null; then
        echo -e "${BLUE}  For Minikube:${NC} minikube addons enable ingress"
    elif command -v kind &> /dev/null && kind get clusters &> /dev/null 2>&1; then
        echo -e "${BLUE}  For Kind:${NC} kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml"
    else
        echo -e "${BLUE}  For Docker Desktop:${NC} kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml"
    fi
    
    echo ""
    read -p "Do you want to continue without Ingress controller? (External access won't work) [y/N]: " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    echo -e "${GREEN}✓ Ingress controller is installed${NC}"
fi

echo ""

# Step 1: Apply Deployment
echo -e "${BLUE}Step 1: Deploying application...${NC}"
kubectl apply -f "${K8S_DIR}/deployment.yaml"
echo -e "${GREEN}✓ Deployment applied${NC}"

# Step 2: Apply Service
echo ""
echo -e "${BLUE}Step 2: Creating service...${NC}"
kubectl apply -f "${K8S_DIR}/service.yaml"
echo -e "${GREEN}✓ Service applied${NC}"

# Step 3: Apply Ingress
echo ""
echo -e "${BLUE}Step 3: Creating ingress...${NC}"
kubectl apply -f "${K8S_DIR}/ingress.yaml"
echo -e "${GREEN}✓ Ingress applied${NC}"

# Step 4: Apply HPA
echo ""
echo -e "${BLUE}Step 4: Creating HorizontalPodAutoscaler...${NC}"
kubectl apply -f "${K8S_DIR}/hpa.yaml"
echo -e "${GREEN}✓ HPA applied${NC}"

# Step 5: Wait for deployment to be ready
echo ""
echo -e "${BLUE}Step 5: Waiting for pods to be ready...${NC}"
echo "This may take a minute or two..."

if kubectl wait --for=condition=available --timeout=180s deployment/currency-api -n ${NAMESPACE}; then
    echo -e "${GREEN}✓ Deployment is ready${NC}"
else
    echo -e "${RED}Error: Deployment did not become ready in time${NC}"
    echo "Check status with: kubectl get pods"
    exit 1
fi

# Step 6: Display deployment information
echo ""
echo -e "${GREEN}=========================================="
echo "Deployment completed successfully!"
echo "==========================================${NC}"
echo ""

# Show pods
echo -e "${BLUE}Pods:${NC}"
kubectl get pods -l app=currency-api

echo ""

# Show service
echo -e "${BLUE}Service:${NC}"
kubectl get service currency-api

echo ""

# Show ingress
echo -e "${BLUE}Ingress:${NC}"
kubectl get ingress currency-api-ingress

echo ""

# Show HPA status
echo -e "${BLUE}HorizontalPodAutoscaler:${NC}"
kubectl get hpa currency-api-hpa

echo ""
echo -e "${BLUE}=========================================="
echo "Access Information"
echo "==========================================${NC}"

# Get ingress access info
if command -v minikube &> /dev/null && minikube status &> /dev/null; then
    MINIKUBE_IP=$(minikube ip)
    echo -e "${GREEN}Minikube IP:${NC} $MINIKUBE_IP"
    echo ""
    echo "To access the API, add this to /etc/hosts:"
    echo "  $MINIKUBE_IP currency-api.local"
    echo ""
    echo "Then access:"
    echo "  http://currency-api.local"
    echo ""
    echo "Or use: minikube service currency-api --url"
else
    echo "Add this to /etc/hosts:"
    echo "  127.0.0.1 currency-api.local"
    echo ""
    echo "Then access:"
    echo "  http://currency-api.local"
fi

echo ""
echo -e "${BLUE}Useful commands:${NC}"
echo "  View logs:        kubectl logs -f deployment/currency-api"
echo "  View HPA status:  kubectl get hpa currency-api-hpa --watch"
echo "  Scale manually:   kubectl scale deployment currency-api --replicas=3"
echo "  Delete all:       ./k8s/delete.sh"

