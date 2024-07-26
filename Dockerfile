FROM openjdk:17-oracle
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} /mc-notification.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/mc-notification.jar"]

