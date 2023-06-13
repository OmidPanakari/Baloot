FROM maven:3.8.7-openjdk-18-slim AS build

WORKDIR /app

COPY src ./src
COPY pom.xml .

RUN mvn clean package

FROM eclipse-temurin:latest

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]