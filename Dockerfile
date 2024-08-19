FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл в контейнер
COPY mcNotification-0.0.1-SNAPSHOT.jar myapp.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8089

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "myapp.jar"]
