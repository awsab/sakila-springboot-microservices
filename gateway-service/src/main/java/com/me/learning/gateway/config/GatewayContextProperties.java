package com.me.learning.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "gateway.context")
public class GatewayContextProperties {

    private String userIdHeader = "X-User-Id";
    private String tenantIdHeader = "X-Tenant-Id";
    private String requestIdHeader = "X-Request-Id";
    private int maxValueLength = 128;
}
