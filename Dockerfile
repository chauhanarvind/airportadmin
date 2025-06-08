# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy everything
COPY . .

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/airport-admin-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR
CMD ["java", "-jar", "app.jar"]
