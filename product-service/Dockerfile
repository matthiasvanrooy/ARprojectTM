FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app
COPY ./pom.xml ./
COPY ./src ./src

EXPOSE 8080

RUN mvn clean package -DskipTests

FROM amazoncorretto:17.0.10
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
