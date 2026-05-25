package com.me.learning.gateway;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "eureka.client.enabled=false"
})
class GatewayRoutesConfigurationTests {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Test
    void shouldLoadConfiguredServiceRoutes() {
        List<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions()
                .collectList()
                .block(Duration.ofSeconds(5));

        assertThat(routeDefinitions).isNotNull();

        List<String> routeIds = routeDefinitions.stream()
                .map(RouteDefinition::getId)
                .toList();

        assertThat(routeIds)
                .contains("catalog-service-route")
                .contains("customer-service-route")
                .contains("inventory-service-route")
                .contains("rental-service-route");

        Map<String, Set<String>> routeFilters = routeDefinitions.stream()
                .collect(Collectors.toMap(
                        RouteDefinition::getId,
                        definition -> definition.getFilters().stream().map(org.springframework.cloud.gateway.filter.FilterDefinition::getName)
                                .collect(Collectors.toSet())
                ));

        assertThat(routeFilters.get("catalog-service-route")).contains("Retry", "CircuitBreaker");
        assertThat(routeFilters.get("customer-service-route")).contains("Retry", "CircuitBreaker");
        assertThat(routeFilters.get("inventory-service-route")).contains("Retry", "CircuitBreaker");
        assertThat(routeFilters.get("rental-service-route")).contains("Retry", "CircuitBreaker");
    }
}

