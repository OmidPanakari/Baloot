FROM openjdk:19-jdk-slim as build

RUN apt-get update && \
    apt-get install -y maven

WORKDIR /app

COPY pom.xml .
RUN mvn -Dhttps.protocols=TLSv1.2 package

COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:19-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]