# Start with a base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Build the JAR
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]