# Start with a base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY gradle gradle
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy the rest of the application
COPY src src

# Build the JAR
RUN ./gradlew bootJar --no-daemon

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]