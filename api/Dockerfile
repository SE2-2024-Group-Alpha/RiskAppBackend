# Step 1: Use a base image with Java and Gradle
FROM gradle:jdk21 as build

WORKDIR /app

COPY --chown=gradle:gradle . /app

RUN gradle bootJar --no-daemon

FROM openjdk:21-jdk-slim

COPY --from=build /app/build/libs/ /app/

EXPOSE 8080

CMD ["java", "-jar", "/app/riskbackend.jar"]
