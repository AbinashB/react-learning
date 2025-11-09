# Use Eclipse Temurin (formerly AdoptOpenJDK) as the base image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/09currencyapi-1.0-SNAPSHOT.jar app.jar

# Expose the port your app runs on (change if needed)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]