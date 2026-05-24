# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.13/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.13/maven-plugin/build-image.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### OpenTelemetry Modes (Enterprise Policy)

This parent POM is configured for a **starter-first** tracing strategy.

- Default mode: `opentelemetry-spring-boot-starter`
- Optional mode: `OTEL_MANUAL_SDK` profile (manual SDK + OTLP exporter)
- Optional mode: `OTEL_MICROMETER_BRIDGE` profile (Micrometer bridge path)

#### 1) Default mode (recommended)

Use `opentelemetry-spring-boot-starter` in child services (no extra profile needed).

```powershell
./mvnw.cmd clean verify -P SIT
```

Starter-mode baseline `application.yaml` example:

```yaml
spring:
  application:
	name: ${APP_NAME:awsab-parent}

otel:
  service:
	name: ${spring.application.name}
  exporter:
	otlp:
	  endpoint: ${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4317}
	  protocol: ${OTEL_EXPORTER_OTLP_PROTOCOL:grpc}

# Optional tuning
management:
  tracing:
	sampling:
	  probability: ${TRACING_SAMPLING_PROBABILITY:1.0}
```

Notes:
- Keep this as the only tracing path for the service when using starter mode.
- Use `OTEL_MANUAL_SDK` only if you intentionally wire SDK/exporter in code.
- Use `OTEL_MICROMETER_BRIDGE` only if you intentionally use the Micrometer bridge path.

#### 2) Manual SDK mode (opt-in)

Use only when a service intentionally wires OpenTelemetry SDK/exporter in code.

```powershell
./mvnw.cmd clean verify -P SIT,OTEL_MANUAL_SDK
```

#### 3) Micrometer bridge mode (opt-in)

Use only when a service intentionally follows Micrometer tracing bridge instead of the OTel starter path.

```powershell
./mvnw.cmd clean verify -P SIT,OTEL_MICROMETER_BRIDGE
```

#### Do / Don't

- Do use one instrumentation mode per service.
- Do keep `opentelemetry-api` only for custom spans where needed.
- Don't combine OTel starter mode with Java agent on the same service unless explicitly designed and tested.
- Don't mix `OTEL_MANUAL_SDK` and `OTEL_MICROMETER_BRIDGE` in the same service unless there is a deliberate architecture reason.

