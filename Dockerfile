# Start with a base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the build output JAR file to the container
COPY build/libs/*-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
