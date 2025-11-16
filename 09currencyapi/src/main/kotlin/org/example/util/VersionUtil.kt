package org.example.util

import java.util.Properties

object VersionUtil {
    private const val VERSION_PROPERTIES_FILE = "version.properties"
    private const val DEFAULT_VERSION = "dev"
    
    private val versionInfo: Properties by lazy {
        val props = Properties()
        try {
            // Try to load version.properties from resources
            val inputStream = javaClass.classLoader.getResourceAsStream(VERSION_PROPERTIES_FILE)
            if (inputStream != null) {
                props.load(inputStream)
                inputStream.close()
            }
        } catch (e: Exception) {
            // Silently fail and use default version
        }
        props
    }
    
    /**
     * Get the application version
     * @return version string from version.properties or "dev" if not found
     */
    fun getVersion(): String {
        return versionInfo.getProperty("version", DEFAULT_VERSION)
    }
    
    /**
     * Get the build time
     * @return build time string or null if not available
     */
    fun getBuildTime(): String? {
        return versionInfo.getProperty("buildTime")
    }
    
    /**
     * Get the group ID
     * @return group ID string or null if not available
     */
    fun getGroupId(): String? {
        return versionInfo.getProperty("groupId")
    }
    
    /**
     * Get all version information as a map
     */
    fun getAllVersionInfo(): Map<String, String> {
        return mapOf(
            "version" to getVersion(),
            "buildTime" to (getBuildTime() ?: "unknown"),
            "groupId" to (getGroupId() ?: "unknown")
        )
    }
}

