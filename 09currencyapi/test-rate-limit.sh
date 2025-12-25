#!/bin/bash

# Test script for Fixed Window Rate Limiting
# Rate limit: 10 requests per minute with burst of 5

echo "=========================================="
echo "Testing Fixed Window Rate Limiting"
echo "Limit: 10 requests/minute + burst of 5"
echo "=========================================="
echo ""

echo "Making 20 rapid requests to /api/usd..."
echo ""

success_count=0
rate_limited_count=0

for i in {1..20}; do
    response=$(curl -s -o /tmp/response.json -w "%{http_code}" http://localhost:8080/api/usd)
    
    if [ "$response" == "200" ]; then
        echo "✅ Request $i: SUCCESS (HTTP 200)"
        ((success_count++))
    elif [ "$response" == "429" ]; then
        error_msg=$(cat /tmp/response.json | jq -r '.message' 2>/dev/null || echo "Rate limit exceeded")
        echo "❌ Request $i: RATE LIMITED (HTTP 429) - $error_msg"
        ((rate_limited_count++))
    else
        echo "⚠️  Request $i: HTTP $response"
    fi
    
    # Small delay to see the pattern
    sleep 0.1
done

echo ""
echo "=========================================="
echo "Results:"
echo "  ✅ Successful requests: $success_count"
echo "  ❌ Rate limited requests: $rate_limited_count"
echo "=========================================="
echo ""

echo "Checking rate limit headers..."
curl -I http://localhost:8080/api/health 2>&1 | grep -E "(HTTP|X-RateLimit|Retry-After)"

echo ""
echo "Waiting 60 seconds for rate limit window to reset..."
echo "(Press Ctrl+C to skip)"
sleep 5

echo ""
echo "Making 3 more requests after waiting..."
for i in {1..3}; do
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/usd)
    if [ "$response" == "200" ]; then
        echo "✅ Request $i: SUCCESS (HTTP 200)"
    else
        echo "❌ Request $i: HTTP $response"
    fi
done

rm -f /tmp/response.json

