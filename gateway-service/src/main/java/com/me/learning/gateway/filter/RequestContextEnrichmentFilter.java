package com.me.learning.gateway.filter;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.MDC;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.api.baggage.BaggageEntryMetadata;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;

import com.me.learning.gateway.config.GatewayContextProperties;

@Component
@RequiredArgsConstructor
public class RequestContextEnrichmentFilter implements GlobalFilter, Ordered {

    public static final String MDC_REQUEST_ID = "requestId";
    public static final String MDC_USER_ID = "userId";
    public static final String MDC_TENANT_ID = "tenantId";

    private static final Pattern UNSAFE_CHARS = Pattern.compile("[^a-zA-Z0-9_@./:-]");

    /**
     * TextMapGetter adapter that reads from a flat {@code Map<String, String>} produced
     * by {@link org.springframework.http.HttpHeaders#toSingleValueMap()}.
     * Using a plain Map avoids Spring-version-specific {@code HttpHeaders} API differences.
     */
    private static final TextMapGetter<Map<String, String>> FLAT_HEADERS_GETTER = new TextMapGetter<>() {
        @Override
        public Iterable<String> keys(Map<String, String> carrier) {
            return carrier == null ? java.util.Collections.emptyList() : carrier.keySet();
        }

        @Override
        public String get(Map<String, String> carrier, String key) {
            return carrier == null ? null : carrier.get(key);
        }
    };

    private final GatewayContextProperties properties;

    @Override
    public reactor.core.publisher.Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = firstNonBlank(
                exchange.getRequest().getHeaders().getFirst(properties.getRequestIdHeader()),
                UUID.randomUUID().toString()
        );

        String userId = sanitize(exchange.getRequest().getHeaders().getFirst(properties.getUserIdHeader()));
        String tenantId = sanitize(exchange.getRequest().getHeaders().getFirst(properties.getTenantIdHeader()));

        // ── OTel Baggage: extract any incoming W3C baggage, then merge our entries ──
        Map<String, String> flatHeaders = exchange.getRequest().getHeaders().toSingleValueMap();
        Context extracted = W3CBaggagePropagator.getInstance()
                .extract(Context.root(), flatHeaders, FLAT_HEADERS_GETTER);

        BaggageBuilder baggageBuilder = Baggage.builder();
        // Copy any existing upstream baggage entries before adding ours
        Baggage.fromContext(extracted).forEach(
                (key, entry) -> baggageBuilder.put(key, entry.getValue(), entry.getMetadata()));

        baggageBuilder.put("request.id", requestId, BaggageEntryMetadata.empty());
        if (StringUtils.hasText(userId)) {
            baggageBuilder.put("user.id", userId, BaggageEntryMetadata.empty());
        }
        if (StringUtils.hasText(tenantId)) {
            baggageBuilder.put("tenant.id", tenantId, BaggageEntryMetadata.empty());
        }

        Context otelContext = Context.root().with(baggageBuilder.build());

        // ── Mutate the request: set per-request headers + inject W3C baggage header ──
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest()
                .mutate()
                .header(properties.getRequestIdHeader(), requestId);

        if (StringUtils.hasText(userId)) {
            requestBuilder.header(properties.getUserIdHeader(), userId);
        }
        if (StringUtils.hasText(tenantId)) {
            requestBuilder.header(properties.getTenantIdHeader(), tenantId);
        }

        W3CBaggagePropagator.getInstance().inject(
                otelContext, requestBuilder, ServerHttpRequest.Builder::header);

        ServerWebExchange mutatedExchange = exchange.mutate().request(requestBuilder.build()).build();
        mutatedExchange.getResponse().getHeaders().set(properties.getRequestIdHeader(), requestId);

        // ── Best-effort MDC for synchronous log statements in this reactive pipeline ──
        return chain.filter(mutatedExchange)
                .doFirst(() -> {
                    MDC.put(MDC_REQUEST_ID, requestId);
                    if (StringUtils.hasText(userId)) {
                        MDC.put(MDC_USER_ID, userId);
                    }
                    if (StringUtils.hasText(tenantId)) {
                        MDC.put(MDC_TENANT_ID, tenantId);
                    }
                })
                .doFinally(signalType -> {
                    MDC.remove(MDC_REQUEST_ID);
                    MDC.remove(MDC_USER_ID);
                    MDC.remove(MDC_TENANT_ID);
                });
    }

    @Override
    @SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String sanitize(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String sanitized = UNSAFE_CHARS.matcher(value.trim()).replaceAll("");
        if (sanitized.length() > properties.getMaxValueLength()) {
            return sanitized.substring(0, properties.getMaxValueLength());
        }
        return sanitized;
    }

    private String firstNonBlank(String first, String fallback) {
        return StringUtils.hasText(first) ? first.trim() : fallback;
    }
}
