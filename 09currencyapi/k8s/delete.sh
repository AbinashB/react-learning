#!/bin/bash

# Delete script for Currency API - Kubernetes deployment
# This script removes all Kubernetes resources created by the deployment

set -e  # Exit on any error

echo "=========================================="
echo "Deleting Currency API from Kubernetes"
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
    exit 1
fi

echo -e "${GREEN}✓ Connected to Kubernetes cluster${NC}"
echo ""

# Check if resources exist
echo -e "${BLUE}Checking for existing resources...${NC}"
RESOURCES_EXIST=false

if kubectl get deployment currency-api -n ${NAMESPACE} &> /dev/null; then
    RESOURCES_EXIST=true
    echo "  - Found Deployment: currency-api"
fi

if kubectl get service currency-api -n ${NAMESPACE} &> /dev/null; then
    RESOURCES_EXIST=true
    echo "  - Found Service: currency-api"
fi

if kubectl get ingress currency-api-ingress -n ${NAMESPACE} &> /dev/null; then
    RESOURCES_EXIST=true
    echo "  - Found Ingress: currency-api-ingress"
fi

if kubectl get hpa currency-api-hpa -n ${NAMESPACE} &> /dev/null; then
    RESOURCES_EXIST=true
    echo "  - Found HPA: currency-api-hpa"
fi

if [ "$RESOURCES_EXIST" = false ]; then
    echo -e "${YELLOW}No Currency API resources found in namespace '${NAMESPACE}'${NC}"
    echo "Nothing to delete."
    exit 0
fi

echo ""

# Confirmation prompt
echo -e "${YELLOW}Warning: This will delete all Currency API resources from Kubernetes${NC}"
read -p "Are you sure you want to continue? [y/N]: " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Deletion cancelled."
    exit 0
fi

echo ""

# Step 1: Delete HPA (so it doesn't interfere with deployment deletion)
echo -e "${BLUE}Step 1: Deleting HorizontalPodAutoscaler...${NC}"
if kubectl delete -f "${K8S_DIR}/hpa.yaml" --ignore-not-found=true; then
    echo -e "${GREEN}✓ HPA deleted${NC}"
else
    echo -e "${YELLOW}! HPA deletion had issues (may not exist)${NC}"
fi

# Step 2: Delete Ingress
echo ""
echo -e "${BLUE}Step 2: Deleting Ingress...${NC}"
if kubectl delete -f "${K8S_DIR}/ingress.yaml" --ignore-not-found=true; then
    echo -e "${GREEN}✓ Ingress deleted${NC}"
else
    echo -e "${YELLOW}! Ingress deletion had issues (may not exist)${NC}"
fi

# Step 3: Delete Service
echo ""
echo -e "${BLUE}Step 3: Deleting Service...${NC}"
if kubectl delete -f "${K8S_DIR}/service.yaml" --ignore-not-found=true; then
    echo -e "${GREEN}✓ Service deleted${NC}"
else
    echo -e "${YELLOW}! Service deletion had issues (may not exist)${NC}"
fi

# Step 4: Delete Deployment
echo ""
echo -e "${BLUE}Step 4: Deleting Deployment...${NC}"
if kubectl delete -f "${K8S_DIR}/deployment.yaml" --ignore-not-found=true; then
    echo -e "${GREEN}✓ Deployment deleted${NC}"
else
    echo -e "${YELLOW}! Deployment deletion had issues (may not exist)${NC}"
fi

# Step 5: Wait for pods to terminate
echo ""
echo -e "${BLUE}Step 5: Waiting for pods to terminate...${NC}"
sleep 2

# Check if any pods are still terminating
if kubectl get pods -l app=currency-api -n ${NAMESPACE} 2>/dev/null | grep -q currency-api; then
    echo "Waiting for pods to fully terminate..."
    kubectl wait --for=delete pod -l app=currency-api -n ${NAMESPACE} --timeout=60s 2>/dev/null || true
fi

echo -e "${GREEN}✓ All pods terminated${NC}"

# Verify deletion
echo ""
echo -e "${BLUE}Verifying deletion...${NC}"

REMAINING=false

if kubectl get deployment currency-api -n ${NAMESPACE} &> /dev/null; then
    echo -e "${RED}  ✗ Deployment still exists${NC}"
    REMAINING=true
fi

if kubectl get service currency-api -n ${NAMESPACE} &> /dev/null; then
    echo -e "${RED}  ✗ Service still exists${NC}"
    REMAINING=true
fi

if kubectl get ingress currency-api-ingress -n ${NAMESPACE} &> /dev/null; then
    echo -e "${RED}  ✗ Ingress still exists${NC}"
    REMAINING=true
fi

if kubectl get hpa currency-api-hpa -n ${NAMESPACE} &> /dev/null; then
    echo -e "${RED}  ✗ HPA still exists${NC}"
    REMAINING=true
fi

if [ "$REMAINING" = true ]; then
    echo ""
    echo -e "${RED}Some resources still exist. You may need to delete them manually.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ All resources deleted successfully${NC}"

echo ""
echo -e "${GREEN}=========================================="
echo "Cleanup completed successfully!"
echo "==========================================${NC}"
echo ""
echo "To redeploy:"
echo "  1. Build: ./k8s/build.sh"
echo "  2. Deploy: ./k8s/deploy.sh"

