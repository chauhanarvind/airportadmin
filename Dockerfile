# ---------- Stage 1: Build the application ----------
FROM eclipse-temurin:21-jdk as build
WORKDIR /app

# Copy your source code
COPY . .

# Run Maven build
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run the application ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/airport-admin-0.0.1-SNAPSHOT.jar app.jar

# Run the app
CMD ["java", "-jar", "app.jar"]
