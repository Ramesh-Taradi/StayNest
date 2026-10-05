# ── Build Stage ──
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy Maven wrapper & POM
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make wrapper executable
RUN chmod +x ./mvnw

# Pre-fetch dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code and build production jar
COPY src src
RUN ./mvnw clean package -DskipTests

# ── Runtime Stage ──
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default port
EXPOSE 8080
ENV PORT=8080

# Run Spring Boot app
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
