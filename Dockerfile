FROM maven:3-openjdk-17 AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM openjdk:17
COPY --from=build /home/app/target/docker-spring-boot.jar /usr/local/lib/app.jar
EXPOSE 8080

ADD ./docker-spring-boot.jar docker-spring-boot.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
