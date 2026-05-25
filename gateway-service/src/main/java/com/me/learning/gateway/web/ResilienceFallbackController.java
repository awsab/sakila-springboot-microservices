package com.me.learning.gateway.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.me.learning.gateway.config.GatewayContextProperties;

@RestController
@RequestMapping("/internal/fallback")
@RequiredArgsConstructor
public class ResilienceFallbackController {

    private static final Pattern UNSAFE_CHARS = Pattern.compile("[^a-zA-Z0-9_@./:-]");

    private final GatewayContextProperties contextProperties;

    @GetMapping("/{service}")
    public ResponseEntity<Map<String, Object>> fallback(@PathVariable String service, ServerHttpRequest request) {
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
