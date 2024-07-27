FROM maven:3.6.3-openjdk-17-slim AS build
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} /mc-notification.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/mc-notification.jar"]

