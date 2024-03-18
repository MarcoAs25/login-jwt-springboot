FROM maven:3-openjdk-17 AS build

RUN mvn install clean install


FROM openjdk:17
COPY --from=build /target/docker-spring-boot.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
