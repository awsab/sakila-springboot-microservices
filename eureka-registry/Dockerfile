FROM eclipse-temurin:25-jre

WORKDIR /app

# Reuse the jar built on the host for faster image rebuilds.
COPY target/eureka-registry-*.jar app.jar

# --- Sensible defaults (runtime config — NOT environment-specific secrets) ---
# These allow the image to work with a plain `docker run` without any flags.
# docker-compose / Kubernetes will override these at deploy-time when needed.

# Default server port — overrideable via -e SERVER_PORT=xxxx at runtime.
ENV SERVER_PORT=8761
# Do not register this server-side registry with itself.
ENV EUREKA_CLIENT_REGISTER_WITH_EUREKA=false
# Do not fetch the registry from itself.
ENV EUREKA_CLIENT_FETCH_REGISTRY=false

# EXPOSE is metadata only — it documents the port but does NOT publish it.
# Actual host-to-container port mapping is always done in docker-compose or `docker run -p`.
EXPOSE 8761

# Launch the Spring Boot application.
ENTRYPOINT ["java", "-jar", "app.jar"]
