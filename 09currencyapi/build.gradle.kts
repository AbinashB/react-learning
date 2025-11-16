import java.time.Instant

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor for HTTP server
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    implementation("io.ktor:ktor-server-cors:2.3.5")
    implementation("io.ktor:ktor-server-call-logging:2.3.5")
    implementation("io.ktor:ktor-server-status-pages:2.3.5")
    
    // Static file serving for Swagger UI (if needed)
    // implementation("io.ktor:ktor-server-webjars:2.3.5")
    
    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")
    
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.ktor:ktor-server-tests:2.3.5")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

application {
    mainClass.set("org.example.ApplicationKt")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Task to generate version.properties file
tasks.register("generateVersionProperties") {
    val propertiesFile = file("${layout.buildDirectory.get().asFile}/resources/main/version.properties")
    outputs.file(propertiesFile)
    
    // Capture values at configuration time to avoid deprecation warnings
    val appVersion = project.version.toString()
    val appGroup = project.group.toString()
    
    doLast {
        propertiesFile.parentFile.mkdirs()
        propertiesFile.writeText("""
            version=$appVersion
            buildTime=${Instant.now()}
            groupId=$appGroup
        """.trimIndent())
    }
}

// Make processResources depend on generateVersionProperties
tasks.named("processResources") {
    dependsOn("generateVersionProperties")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.example.ApplicationKt"
        attributes["Implementation-Version"] = project.version
    }
    // Create a fat JAR with all dependencies
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}