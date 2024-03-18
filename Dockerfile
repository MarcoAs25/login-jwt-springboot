FROM maven:3-openjdk-17 AS build
COPY . .
RUN mvn clean package

FROM openjdk:17
EXPOSE ${PORT}
COPY --from=build /target/docker-spring-boot.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]