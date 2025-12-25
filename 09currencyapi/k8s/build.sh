#!/bin/bash

# Build script for Currency API - Kubernetes deployment
# This script builds the JAR and Docker image, then loads it into the local cluster

set -e  # Exit on any error

echo "=========================================="
echo "Building Currency API for Kubernetes"
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
IMAGE_NAME="currency-api"
IMAGE_TAG="latest"
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

echo -e "${BLUE}Project root: ${PROJECT_ROOT}${NC}"

# Step 1: Build JAR using Gradle
echo ""
echo -e "${BLUE}Step 1: Building JAR with Gradle...${NC}"
cd "$PROJECT_ROOT"

# Clean and build
./gradlew clean build -x test

if [ ! -f "build/libs/09currencyapi-1.0-SNAPSHOT.jar" ]; then
    echo -e "${RED}Error: JAR file not found after build!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ JAR built successfully${NC}"

# Step 2: Build Docker image
echo ""
echo -e "${BLUE}Step 2: Building Docker image...${NC}"
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Docker build failed!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Docker image built successfully${NC}"

# Step 3: Load image into local Kubernetes cluster
echo ""
echo -e "${BLUE}Step 3: Loading image into local Kubernetes cluster...${NC}"

# Detect which local cluster is being used
if command -v minikube &> /dev/null && minikube status &> /dev/null; then
    echo "Detected Minikube cluster"
    minikube image load ${IMAGE_NAME}:${IMAGE_TAG}
    echo -e "${GREEN}✓ Image loaded into Minikube${NC}"
    
elif command -v kind &> /dev/null && kind get clusters &> /dev/null 2>&1; then
    # Get the first kind cluster
    CLUSTER_NAME=$(kind get clusters | head -n 1)
    if [ -n "$CLUSTER_NAME" ]; then
        echo "Detected Kind cluster: $CLUSTER_NAME"
        kind load docker-image ${IMAGE_NAME}:${IMAGE_TAG} --name $CLUSTER_NAME
        echo -e "${GREEN}✓ Image loaded into Kind cluster${NC}"
    else
        echo -e "${RED}No Kind clusters found${NC}"
        exit 1
    fi
    
elif docker info 2>&1 | grep -q "Kubernetes"; then
    echo "Detected Docker Desktop Kubernetes"
    echo -e "${GREEN}✓ Image already available in Docker Desktop${NC}"
    
else
    echo -e "${RED}Error: No local Kubernetes cluster detected!${NC}"
    echo "Please ensure one of the following is running:"
    echo "  - Minikube (minikube start)"
    echo "  - Kind (kind create cluster)"
    echo "  - Docker Desktop with Kubernetes enabled"
    exit 1
fi

# Step 4: Verify image
echo ""
echo -e "${BLUE}Step 4: Verifying image...${NC}"
docker images | grep ${IMAGE_NAME}

echo ""
echo -e "${GREEN}=========================================="
echo "Build completed successfully!"
echo "==========================================${NC}"
echo ""
echo "Next steps:"
echo "  1. Deploy to Kubernetes: ./k8s/deploy.sh"
echo "  2. Or apply manually: kubectl apply -f k8s/"

