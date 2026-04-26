# --- Stage 1: GraalVM native build ---
FROM ghcr.io/graalvm/native-image-community:21 AS builder

WORKDIR /app
COPY . .

RUN ./gradlew nativeCompile --no-daemon -x test

# --- Stage 2: Minimal runtime ---
FROM debian:stable-slim

WORKDIR /app
COPY --from=builder /app/build/native/nativeCompile/payment-notifications /app/payment-notifications

EXPOSE 8081
ENTRYPOINT ["/app/payment-notifications"]
