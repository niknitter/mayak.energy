FROM openjdk:17-jdk-slim

RUN mkdir /app

COPY target/application.jar /app/application.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
