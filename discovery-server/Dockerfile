# Dockerfile discovery-service

# -------- Build stage --------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only pom.xml to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests -B

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Use docker profile if needed
ENV SPRING_PROFILES_ACTIVE=docker

# Expose Eureka port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]