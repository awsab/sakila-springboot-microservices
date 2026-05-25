package com.me.learning.gateway;

import java.time.Duration;
import java.util.List;

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
        List<String> routeIds = routeDefinitionLocator.getRouteDefinitions()
                .map(RouteDefinition::getId)
                .collectList()
                .block(Duration.ofSeconds(5));

        assertThat(routeIds)
                .contains("catalog-service-route")
                .contains("customer-service-route")
                .contains("inventory-service-route")
                .contains("rental-service-route");
    }
}

