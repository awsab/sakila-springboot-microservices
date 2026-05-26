package com.me.learning.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Centralized OpenAPI 3.0 (Swagger) documentation for the API Gateway.
 *
 * <p>
 * This configuration exposes interactive API docs at:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - ReDoc: http://localhost:8080/redoc.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 *
 * <p>
 * Routes are automatically grouped by service using {@link GroupedOpenApi} beans,
 * allowing clients to navigate each microservice's APIs independently.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Global OpenAPI metadata — title, version, description, contact, license, servers.
     * Accessed via GitProperties and BuildProperties injected from git.properties and
     * application.properties (populated by spring-boot-maven-plugin).
     */
    @Bean
    public OpenAPI customOpenAPI(BuildProperties buildProperties,
                                  GitProperties gitProperties) {
        return new OpenAPI()
                .info(apiInfo(buildProperties, gitProperties))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local development"))
                .addServersItem(new Server()
                        .url("${GATEWAY_URL:https://api.example.com}")
                        .description("Production gateway (override via GATEWAY_URL env var)"));
    }

    /**
     * Build the Info object with project metadata, git commit info, and contact/license details.
     */
    private Info apiInfo(BuildProperties buildProperties, GitProperties gitProperties) {
        return new Info()
                .title("Sakila Microservices API Gateway")
                .version(buildProperties.getVersion())
                .description(
                        "Centralized API Gateway for the Sakila microservices ecosystem.\n\n" +
                        "**Key Features:**\n" +
                        "- Reactive WebFlux gateway with Spring Cloud Gateway\n" +
                        "- Circuit breaker + Retry resilience patterns (Resilience4J)\n" +
                        "- Service discovery via Netflix Eureka\n" +
                        "- Distributed tracing with OpenTelemetry\n" +
                        "- Request/User/Tenant correlation via baggage + MDC\n" +
                        "- Graceful fallback endpoints for circuit-open scenarios\n\n" +
                        "**Build Info:**\n" +
                        "- Commit: " + gitProperties.get("commit.id.abbrev") + "\n" +
                        "- Branch: " + gitProperties.get("branch") + "\n" +
                        "- Build Time: " + buildProperties.getTime()
                )
                .contact(new Contact()
                        .name("API Gateway Team")
                        .email("gateway-team@example.com")
                        .url("https://github.com/example/sakila-microservices"))
                .license(new License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    /**
     * GroupedOpenApi for the <b>catalog-service</b> routes.
     * Routes: /catalog/**
     */
    @Bean
    public GroupedOpenApi catalogServiceApi() {
        return GroupedOpenApi.builder()
                .group("catalog-service")
                .displayName("Catalog Service")
                .pathsToMatch("/catalog/**")
                .description("Product catalog APIs: browse products, search, filter by category/price.")
                .build();
    }

    /**
     * GroupedOpenApi for the <b>customer-service</b> routes.
     * Routes: /customer/**
     */
    @Bean
    public GroupedOpenApi customerServiceApi() {
        return GroupedOpenApi.builder()
                .group("customer-service")
                .displayName("Customer Service")
                .pathsToMatch("/customer/**")
                .description("Customer management APIs: registration, profile, preferences, order history.")
                .build();
    }

    /**
     * GroupedOpenApi for the <b>inventory-service</b> routes.
     * Routes: /inventory/**
     */
    @Bean
    public GroupedOpenApi inventoryServiceApi() {
        return GroupedOpenApi.builder()
                .group("inventory-service")
                .displayName("Inventory Service")
                .pathsToMatch("/inventory/**")
                .description("Inventory tracking APIs: stock levels, reservations, warehouse management.")
                .build();
    }

    /**
     * GroupedOpenApi for the <b>rental-service</b> routes.
     * Routes: /rental/**
     */
    @Bean
    public GroupedOpenApi rentalServiceApi() {
        return GroupedOpenApi.builder()
                .group("rental-service")
                .displayName("Rental Service")
                .pathsToMatch("/rental/**")
                .description("Rental management APIs: bookings, availability, pricing, customer agreements.")
                .build();
    }

    /**
     * GroupedOpenApi for the <b>internal/system</b> endpoints.
     * Routes: /internal/**
     */
    @Bean
    public GroupedOpenApi internalApi() {
        return GroupedOpenApi.builder()
                .group("internal-system")
                .displayName("Internal / System")
                .pathsToMatch("/internal/**")
                .description("Internal system endpoints: circuit-breaker fallbacks, health checks, actuator.")
                .build();
    }
}

