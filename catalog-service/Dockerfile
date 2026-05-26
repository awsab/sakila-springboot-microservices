# ─── Stage 1: Build ───────────────────────────────────────────────────────────
# Maven + JDK 25 build environment (matches the project's Java version).
###FROM maven:3.9.11-eclipse-temurin-25 AS build

#WORKDIR /workspace

# Copy whole monorepo so sakila-parent and sakila-framework are available locally.
###COPY . .

# Build only catalog-service from the root reactor; -am builds upstream modules as needed.
###RUN mvn -f pom.xml -pl catalog-service -am -DskipTests clean package

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
# Lean JRE-only base image — no Maven or JDK tools in the final container.
FROM eclipse-temurin:25-jre

WORKDIR /app

# Pull in the executable Spring Boot jar from the build stage.
###COPY --from=build /workspace/catalog-service/target/catalog-service-0.0.1-SNAPSHOT.jar app.jar
# Reuse the jar built on the host for faster image rebuilds.
COPY target/catalog-service-*.jar app.jar

# --- Sensible defaults (overrideable at runtime via compose / k8s / -e flag) ---
# Default port matching application.yaml — no rebuild needed to change for a different env.
ENV SERVER_PORT=8091
# Default to DEV profile (safe local default); compose/k8s injects uat/prod values at deploy-time.
ENV SPRING_PROFILES_ACTIVE=dev
# Default Eureka URL works for bare `docker run` on host network.
# Compose overrides this to use the container DNS name (eureka-registry).
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# EXPOSE is metadata only — documents the port, does not publish it to the host.
EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
