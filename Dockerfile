FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Make gradlew executable and build the JAR
RUN chmod +x ./gradlew && \
    ./gradlew bootJar --no-daemon

# Find the exact name of your JAR file
RUN find build/libs/ -type f -name "*.jar" -exec cp {} app.jar \;

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]