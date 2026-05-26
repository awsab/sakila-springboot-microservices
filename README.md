# sakila-springboot-microservices

## Profile Guide

This project uses three Spring profiles across all services:

| Profile | DB | Flyway | Hibernate DDL | Tests in Maven profile |
| --- | --- | --- | --- | --- |
| `dev` | H2 (in-memory) | disabled | `create-drop` | enabled (`DEV`) |
| `uat` | MySQL | enabled | `validate` | enabled (`UAT`) |
| `prod` | MySQL | enabled | `validate` | skipped (`PROD`) |

## Runtime Defaults

- Default active profile is `dev`.
- Docker images default to `SPRING_PROFILES_ACTIVE=dev` for local runs.
- Eureka and baseline logging defaults are kept in each service `application.yaml` for explicit ownership.

## Common Commands

```powershell
mvn clean verify -P DEV
mvn clean verify -P UAT
mvn clean package -P PROD
```

To run an individual service with a profile:

```powershell
mvn -f catalog-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=dev
mvn -f catalog-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=uat
mvn -f catalog-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=prod
```

