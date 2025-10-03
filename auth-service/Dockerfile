# # syntax=docker/dockerfile:1

# # -------- Build stage --------
# FROM maven:3.9.6-eclipse-temurin-17 AS build
# WORKDIR /app

# # Leverage dependency caching
# COPY .mvn/ .mvn/
# COPY mvnw.cmd mvnw pom.xml ./
# RUN mvn -q -B -DskipTests dependency:go-offline

# # Build
# COPY src/ src/
# RUN mvn -q -B -DskipTests package

# # -------- Runtime stage --------
# FROM eclipse-temurin:17-jre
# WORKDIR /app

# # Copy the built jar from the build stage
# ARG JAR_FILE=target/*.jar
# COPY --from=build /app/${JAR_FILE} app.jar

# # Note: container port 8081 by default for Spring Boot unless overridden
# EXPOSE 8081

# ENTRYPOINT ["java","-jar","/app/app.jar"]


 
# Use Eclipse Temurin JDK 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copy project files and build the application
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests for faster builds). Uses a cached Maven repo for speed.
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

# Runtime image with JRE 21 (smaller than full JDK)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Install curl for container healthcheck
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Copy the built JAR from the builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Expose application port
EXPOSE 8081

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
