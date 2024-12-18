# Use a base image with Java 17 runtime
FROM openjdk:17-slim

# Add application JAR to the image
ARG JAR_FILE=build/libs/spring-patients-app.jar
COPY build/libs/*-SNAPSHOT.jar app.jar

# Expose the port your app listens on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
