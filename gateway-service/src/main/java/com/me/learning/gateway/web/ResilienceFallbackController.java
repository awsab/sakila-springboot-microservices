package com.me.learning.gateway.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.me.learning.gateway.config.GatewayContextProperties;

/**
 * Resilience fallback endpoint invoked when a downstream service's circuit breaker is open
 * or a request times out.
 *
 * <p>
 * This endpoint is called by Spring Cloud Gateway's {@link org.springframework.cloud.gateway.filter.factory.CircuitBreakerFilterFactory}
 * when {@code fallbackUri: forward:/internal/fallback/{service}} is configured on a route.
 *
 * <p>
 * Returns a standardized 503 Service Unavailable response with correlation headers and
 * diagnostic metadata.
 */
@RestController
@RequestMapping("/internal/fallback")
@RequiredArgsConstructor
@Tag(name = "Resilience Fallback", description = "Circuit-breaker fallback endpoints for degraded service scenarios")
public class ResilienceFallbackController {

    private static final Pattern UNSAFE_CHARS = Pattern.compile("[^a-zA-Z0-9_@./:-]");

    private final GatewayContextProperties contextProperties;

    /**
     * Handle circuit-breaker fallback when a downstream service is unavailable.
     *
     * <p>
     * Invoked when:
     * - Circuit breaker reaches the open state (failure rate threshold exceeded)
     * - Request times out (exceeds resilience4j timeout)
     * - HTTP errors or connection failures occur beyond retry count
     *
     * <p>
     * Returns a structured 503 response with correlation IDs matching the original request,
     * allowing clients and observability systems to trace the failure chain.
     *
     * @param service the downstream microservice name (catalog, customer, inventory, rental)
     * @param request the HTTP request carrying optional X-User-Id, X-Tenant-Id, X-Request-Id headers
     * @return ResponseEntity with 503 status and diagnostic payload
     */
    @GetMapping("/{service}")
    @Operation(
            summary = "Circuit-breaker fallback for unavailable service",
            description = "Returns graceful 503 response when a downstream service is unreachable due to " +
                    "circuit breaker open, timeouts, or repeated failures. Includes correlation IDs for tracing."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable — the requested downstream service is temporarily degraded",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "timestamp": "2026-05-25T10:30:45.123Z",
                                      "status": 503,
                                      "error": "SERVICE_UNAVAILABLE",
                                      "service": "catalog-service",
                                      "message": "Service is temporarily unavailable. Please retry later.",
                                      "path": "/catalog/products",
                                      "requestId": "550e8400-e29b-41d4-a716-446655440000",
                                      "userId": "user-123",
                                      "tenantId": "tenant-99"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<Map<String, Object>> fallback(
            @Parameter(
                    name = "service",
                    description = "Microservice name (catalog, customer, inventory, rental)",
                    required = true,
                    example = "catalog-service"
            )
            @PathVariable String service,
            ServerHttpRequest request) {
        String requestId = sanitize(request.getHeaders().getFirst(contextProperties.getRequestIdHeader()));
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        String userId = request.getHeaders().getFirst(contextProperties.getUserIdHeader());
        String tenantId = request.getHeaders().getFirst(contextProperties.getTenantIdHeader());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("timestamp", Instant.now().toString());
        payload.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        payload.put("error", "SERVICE_UNAVAILABLE");
        payload.put("service", service);
        payload.put("message", "Service is temporarily unavailable. Please retry later.");
        payload.put("path", request.getPath().value());
        payload.put("requestId", requestId);
        payload.put("userId", userId);
        payload.put("tenantId", tenantId);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header(contextProperties.getRequestIdHeader(), requestId)
                .body(payload);
    }

    private String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String sanitized = UNSAFE_CHARS.matcher(value.trim()).replaceAll("");
        int maxLength = contextProperties.getMaxValueLength();
        if (sanitized.length() > maxLength) {
            return sanitized.substring(0, maxLength);
        }
        return sanitized;
    }
}
