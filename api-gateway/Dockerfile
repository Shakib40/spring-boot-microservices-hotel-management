# syntax=docker/dockerfile:1

# -------- Build stage --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Leverage dependency caching
COPY .mvn/ .mvn/
COPY mvnw.cmd mvnw pom.xml ./
RUN mvn -q -B -DskipTests dependency:go-offline

# Build
COPY src/ src/
RUN mvn -q -B -DskipTests package

# -------- Runtime stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

# Note: container port 8080 by default for Spring Boot unless overridden
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
