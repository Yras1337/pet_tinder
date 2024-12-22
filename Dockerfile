# Используем официальный образ Maven с OpenJDK 11
FROM maven:3.8.4-openjdk-11-slim AS build

# Устанавливаем рабочую директорию для сборки
WORKDIR /app

# Копируем pom.xml и весь проект в контейнер
COPY pom.xml .
COPY src ./src

# Собираем проект
RUN mvn clean install

# Используем образ с JRE для запуска приложения
FROM openjdk:11-jre-slim

# Копируем собранный .jar файл из предыдущего этапа
COPY --from=build /app/target/pet_tinder-0.0.1-SNAPSHOT.jar /app/pet_tinder-0.0.1-SNAPSHOT.jar

# Запускаем приложение
CMD ["java", "-jar", "/app/pet_tinder-0.0.1-SNAPSHOT.jar"]
