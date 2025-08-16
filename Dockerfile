# ---------- STAGE 1: Build the application ----------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies (for cache efficiency)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build
COPY . .
RUN mvn clean package -DskipTests

# ---------- STAGE 2: Run the application ----------
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=builder /app/target/forex_rates-0.0.1-SNAPSHOT.jar app.jar

# Expose your app's port
EXPOSE 5300

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

# use this to build the project on Mac
# docker build --platform=linux/amd64 -t forex_rates-app .

# to run the image use this
# docker run -p 5300:5300 forex_rates-app

