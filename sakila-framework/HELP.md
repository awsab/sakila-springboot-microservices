# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.5/maven-plugin/build-image.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### AWSAB Logging Include (for consumer services)

If a consumer project uses a custom `logback-spring.xml`, it can include the framework logging fragment:

```xml
<configuration>
    <include resource="logback-awsab-include.xml"/>

    <!-- service-specific logger/root overrides -->
</configuration>
```

#### When to use this include

- Use it when the consumer maintains its own `logback-spring.xml` and wants AWSAB JSON logging behavior.
- If the consumer does not define a custom logback XML, AWSAB logging is already applied automatically by `AwsabLoggingApplicationListener`.

#### Consumer properties

These can be set in the consumer `application.yaml`:

```yaml
awsab:
  logging:
    json-format-enabled: true
    logstash-enabled: false
```

Notes:

- `json-format-enabled` defaults to `true`.
- `logstash-enabled` defaults to `false`.
- Avoid registering duplicate JSON appenders in consumer XML; either include this fragment or define a custom equivalent once.

### AWSAB Spring Boot Auto-Configuration

#### How it is registered (Boot 3)

`awsab-framework` uses Boot 3 registration files:

- Auto-configuration class registration:
  - `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  - contains `com.me.learning.framework.AwsabFrameworkAutoConfiguration`
- Early startup listener registration:
  - `META-INF/spring.factories`
  - registers `com.me.learning.framework.logging.AwsabLoggingApplicationListener`

#### What gets auto-configured

When the dependency is on the classpath, `AwsabFrameworkAutoConfiguration` contributes shared beans such as:

- `GlobalExceptionHandler`
- Jackson modules/customizer (`JavaTimeModule`, `Jdk8Module`, JSON defaults)
- `Clock` (UTC)
- `LinkHeaderUtil`
- `ColumnConverterReactive`
- Date converter beans from `DateConverters`

It also enables binding for logging properties via `AwsabLoggingProperties` (`awsab.logging.*`).

#### Consumer override model

Framework beans are declared with `@ConditionalOnMissingBean`, so consumer services can replace defaults by defining their own bean of the same type.

#### Quick verification in a consumer service

1. Add dependency: `com.me.learning.parent:awsab-framework`
2. Start the service and confirm startup logs include framework auto-configuration messages.
3. Confirm expected beans are present in the application context.
4. Optionally set `debug=true` and inspect condition evaluation output to see auto-config matches.

### Complete Framework Guide

For complete framework documentation (all utilities, components, and usage examples), see:

- `FRAMEWORK_GUIDE.md`
