FROM eclipse-temurin:25-jre

WORKDIR /app

# Reuse the jar built on the host for faster image rebuilds.
COPY target/gateway-service-*.jar app.jar

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