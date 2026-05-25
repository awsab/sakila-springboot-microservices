# ─── Stage 1: Build ───────────────────────────────────────────────────────────
# Use Maven + JDK 25 to compile and package the module.
FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /workspace

# Copy the whole monorepo so parent and framework modules are resolvable during build.
COPY . .

# Build only gateway-service from the root reactor; -am builds upstream modules as needed.
RUN mvn -f pom.xml -pl gateway-service -am -DskipTests clean package

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
# Use a minimal JRE-only image for the final container (no build tool, smaller image
FROM eclipse-temurin:25-jre

WORKDIR /app
# Copy the executable fat-jar produced by Spring Boot Maven Plugin.
COPY --from=build /workspace/gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

# --- Sensible defaults (runtime config — NOT environment-specific secrets) ---
# These allow the image to work with a plain `docker run` without any flags.
# docker-compose / Kubernetes will override these at deploy-time when needed.
# Default server port — overrideable via -e SERVER_PORT=xxxx at runtime.
ENV SERVER_PORT=8080
# Register with Eureka server at this URL by default; overrideable via -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=xxxx at runtime.
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-registry:8761/eureka/
# Register this gateway with Eureka so it can discover other services and be discovered itself.
ENV EUREKA_CLIENT_REGISTER_WITH_EUREKA=true
# Do not fetch the registry from itself (it won't be in the registry).
ENV EUREKA_CLIENT_FETCH_REGISTRY=false

# EXPOSE is metadata only — it documents the port but does NOT publish it.
# Actual host-to-container port mapping is always done in docker-compose or `docker run -p
EXPOSE 8080

# Launch the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]