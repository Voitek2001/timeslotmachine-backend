FROM gradle:8.12.0-jdk17-corretto-al2023 AS builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle build bootJar

FROM amazoncorretto:17.0.13-alpine3.19
WORKDIR /app
EXPOSE 8080
COPY --from=builder /builder/build/libs/backend-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
