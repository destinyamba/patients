# Start with a base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Make gradlew executable and build the JAR
RUN chmod +x ./gradlew && \
    ./gradlew bootJar --no-daemon

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]