# Automatic Version Management Implementation

## Overview
Implemented automatic version management for the Currency Converter API. The application version is now automatically read from `build.gradle.kts` and included in health check endpoints.

## Changes Made

### 1. Build Configuration (`build.gradle.kts`)
- Added a Gradle task `generateVersionProperties` that creates a `version.properties` file at build time
- The file contains:
  - `version`: Project version from `build.gradle.kts`
  - `buildTime`: Timestamp when the build was created
  - `groupId`: Project group ID
- Made `processResources` depend on this task to ensure the file is generated before compilation
- Added version to JAR manifest as `Implementation-Version`

### 2. Version Utility Class (`src/main/kotlin/org/example/util/VersionUtil.kt`)
- Created a utility object that loads the `version.properties` file from classpath at runtime
- Provides methods to access:
  - `getVersion()`: Returns the application version (or "dev" if not found)
  - `getBuildTime()`: Returns the build timestamp
  - `getGroupId()`: Returns the project group ID
  - `getAllVersionInfo()`: Returns all version information as a map
- Uses lazy initialization for efficient resource loading
- Includes debug logging to help troubleshoot loading issues

### 3. Data Model Update (`src/main/kotlin/org/example/model/CurrencyResponse.kt`)
- Updated `HealthResponse` data class to include an optional `version` field
- Made the field nullable to maintain backward compatibility

### 4. Controller Update (`src/main/kotlin/org/example/controller/CurrencyController.kt`)
- Updated both health check endpoints (`/` and `/api/health`) to include the version
- Uses `VersionUtil.getVersion()` to fetch the version dynamically

### 5. JSON Serialization Configuration (`src/main/kotlin/org/example/config/PluginConfig.kt`)
- Added `explicitNulls = false` to exclude null fields from JSON output
- Added `encodeDefaults = true` to include default values in JSON serialization
- Added `@OptIn(ExperimentalSerializationApi::class)` to handle experimental API usage

## How It Works

1. **Build Time**: When you run `./gradlew build`, the `generateVersionProperties` task:
   - Reads the version from `build.gradle.kts`
   - Generates a `version.properties` file in `build/resources/main/`
   - This file gets included in the JAR

2. **Runtime**: When the application starts:
   - `VersionUtil` loads `version.properties` from the classpath
   - The health endpoints use `VersionUtil.getVersion()` to get the current version
   - The version is returned in the JSON response

3. **Version Update**: To change the version:
   - Simply update the `version` in `build.gradle.kts` (e.g., from `1.0-SNAPSHOT` to `1.1.0`)
   - Rebuild the application with `./gradlew build`
   - The new version will automatically be used everywhere

## Testing

All health check endpoints now return the version:

```bash
# Root health check
curl http://localhost:8080/
# Response: {"status":"UP","service":"Currency Converter API","version":"1.0-SNAPSHOT"}

# API health check
curl http://localhost:8080/api/health
# Response: {"status":"UP","service":"Currency Converter API","version":"1.0-SNAPSHOT"}

# Direct instance checks
curl http://localhost:8081/  # Instance 1
curl http://localhost:8082/  # Instance 2
```

## Benefits

1. **Single Source of Truth**: Version is defined only in `build.gradle.kts`
2. **Automatic Updates**: No need to manually update version strings in code
3. **Build Metadata**: Includes build time for tracking deployments
4. **No Runtime Dependencies**: Uses standard Java Properties API
5. **Fallback Support**: Returns "dev" if version.properties is not found (useful during development)
6. **Docker Compatible**: Works seamlessly with Docker containers

## Files Modified

1. `build.gradle.kts` - Added version properties generation task
2. `src/main/kotlin/org/example/util/VersionUtil.kt` - New utility class
3. `src/main/kotlin/org/example/model/CurrencyResponse.kt` - Updated HealthResponse model
4. `src/main/kotlin/org/example/controller/CurrencyController.kt` - Updated health endpoints
5. `src/main/kotlin/org/example/config/PluginConfig.kt` - Updated JSON serialization config

## Build and Deployment

```bash
# Clean build
./gradlew clean build

# Rebuild Docker containers
docker-compose build
docker-compose up -d
```

The version will be automatically included in all builds and deployments.

