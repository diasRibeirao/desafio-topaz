# --- Build stage -------------------------------------------------------------

FROM maven:3.8-openjdk-8 AS build

WORKDIR /build

COPY pom.xml .

RUN mvn -q -B dependency:go-offline || true

COPY src ./src

RUN mvn -q -B clean package -DskipTests


# --- Runtime stage -----------------------------------------------------------

FROM jboss/wildfly:10.1.0.Final

COPY --from=build \
    /build/target/url-encurtador.war \
    /opt/jboss/wildfly/standalone/deployments/url-encurtador.war

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]