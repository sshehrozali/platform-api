# --- Stage 1: Build Stage (Compile Java) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: Runtime Stage (Java + Pulumi) ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Pulumi local state dir (used when PULUMI_BACKEND_URL=file:///app/pulumi-state)
RUN mkdir -p /app/pulumi-state

# 1. Install system tools needed for Pulumi
RUN apt-get update && apt-get install -y \
    curl \
    git \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# 2. Install Pulumi CLI
# The Automation API requires the binary to be in the system PATH
RUN curl -fsSL https://get.pulumi.com | sh
ENV PATH="/root/.pulumi/bin:${PATH}"

# 3. Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# 4. Expose the port your Spring Boot app uses (default 8080)
EXPOSE 8080

# 5. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
