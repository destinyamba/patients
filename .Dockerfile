# Use a base image with Java 17 runtime
FROM openjdk:17-jre-slim

# Add application JAR to the image
ARG JAR_FILE=build/libs/spring-patients-app.jar
COPY ${JAR_FILE} app.jar

# Expose the port your app listens on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
