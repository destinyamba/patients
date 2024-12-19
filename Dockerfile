# Start with a base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

COPY . .

# Build the JAR
RUN ./gradlew clean bootJar

# Copy the build output JAR file to the container
COPY build/libs/app.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]