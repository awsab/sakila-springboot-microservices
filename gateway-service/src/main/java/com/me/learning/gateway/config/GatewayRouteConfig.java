package com.me.learning.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("code-routes")
public class GatewayRouteConfig {

    @Bean
    public RouteLocator serviceRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("catalog-service-route", route -> route
                        .path("/catalog/**")
                        .filters(filter -> filter.stripPrefix(1))
                        .uri("lb://catalog-service"))
                .route("customer-service-route", route -> route
                        .path("/customer/**")
                        .filters(filter -> filter.stripPrefix(1).prefixPath("/customer-identity/api"))
                        .uri("lb://customer-service"))
                .route("inventory-service-route", route -> route
                        .path("/inventory/**")
                        .filters(filter -> filter.stripPrefix(1))
                        .uri("lb://inventory-service"))
                .route("rental-service-route", route -> route
                        .path("/rental/**")
                        .filters(filter -> filter.stripPrefix(1).prefixPath("/rental/api"))
                        .uri("lb://rental-service"))
                .build();
    }
}

