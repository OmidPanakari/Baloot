FROM maven:3.9.0-eclipse-temurin-19 AS build

WORKDIR /app

COPY src ./src
COPY pom.xml .

RUN mvn clean package

FROM eclipse-temurin:latest

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]