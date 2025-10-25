# --- Stage 1: Gradle build ---
FROM gradle:8.4.0-jdk21 AS builder

WORKDIR /app
COPY . .

# Build the application using the Gradle wrapper
RUN ./gradlew bootJar --no-daemon

# --- Stage 2: Slim JVM runtime ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]