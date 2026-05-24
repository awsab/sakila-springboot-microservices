# ─── Stage 1: Build ───────────────────────────────────────────────────────────
# Maven + JDK 25 to compile the module and all its required parent/framework deps.
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /workspace

# Full repo copy needed because customer-service inherits sakila-parent / sakila-framework.
COPY . .

# Build only customer-service from the root reactor; -am builds upstream modules as needed.
RUN mvn -f pom.xml -pl customer-service -am -DskipTests clean package

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
# Minimal JRE image — no Maven, no JDK, just what's needed to run the jar.
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the packaged Spring Boot fat-jar from the build result.
COPY --from=build /workspace/customer-service/target/customer-service-0.0.1-SNAPSHOT.jar app.jar

# --- Sensible defaults (overrideable at runtime — never hardcode secrets here) ---
# Default port from application.yaml.
ENV SERVER_PORT=8081
# Default Spring profile for local/SIT. Compose/k8s sets this for UAT/PROD at deploy-time.
ENV SPRING_PROFILES_ACTIVE=sit
# Eureka URL default for bare `docker run`; compose overrides to use container service name.
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# EXPOSE documents the listening port — actual host publishing done in compose or `docker run -p`.
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
