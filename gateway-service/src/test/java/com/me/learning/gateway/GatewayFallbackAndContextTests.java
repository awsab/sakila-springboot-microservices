package com.me.learning.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "eureka.client.enabled=false"
        }
)
class GatewayFallbackAndContextTests {

    @LocalServerPort
    private int port;

    @Test
    void shouldReturnFallbackPayloadWithContextHeaders() {
        EntityExchangeResult<byte[]> result = webTestClient().get()
                .uri("/internal/fallback/catalog-service")
                .header("X-User-Id", "user-123")
                .header("X-Tenant-Id", "tenant-99")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectHeader().exists("X-Request-Id")
                .expectBody()
                .jsonPath("$.service").isEqualTo("catalog-service")
                .jsonPath("$.status").isEqualTo(503)
                .jsonPath("$.error").isEqualTo("SERVICE_UNAVAILABLE")
                .jsonPath("$.userId").isEqualTo("user-123")
                .jsonPath("$.tenantId").isEqualTo("tenant-99")
                .returnResult();

        assertNotNull(result.getResponseBody(),
                "Fallback response body must not be null");
        assertEquals(503, result.getStatus().value(),
                "Fallback HTTP status must be 503 SERVICE_UNAVAILABLE");
        assertNotNull(result.getResponseHeaders().getFirst("X-Request-Id"),
                "X-Request-Id correlation header must be present in the fallback response");
    }

    @Test
    void shouldGenerateRequestIdWhenMissing() {
        EntityExchangeResult<byte[]> result = webTestClient().get()
                .uri("/internal/fallback/inventory-service")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectHeader().exists("X-Request-Id")
                .expectBody()
                .returnResult();

        assertNotNull(result.getResponseHeaders().getFirst("X-Request-Id"),
                "Gateway must auto-generate X-Request-Id when the request carries none");
    }

    private WebTestClient webTestClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }
}
