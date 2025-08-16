# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-21 AS pre-builder
WORKDIR /build
COPY . .
RUN mvn clean package

# Stage 2: Copy JAR into Keycloak
FROM quay.io/keycloak/keycloak:26.3.2 as builder

# copy the jars ...
COPY --from=pre-builder /build/slv-keycloak-rest/target/com.soloval.tech.slv-keycloak-rest-*.jar /opt/keycloak/providers/
COPY --from=pre-builder /build/slv-keycloak-core/target/com.soloval.tech.slv-keycloak-core-*.jar /opt/keycloak/providers/
COPY --from=pre-builder /build/slv-keycloak-ui/target/com.soloval.tech.slv-keycloak-ui-*.jar /opt/keycloak/providers/
