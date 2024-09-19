FROM openjdk:17-jdk-slim

RUN mkdir /app

COPY target/mayak.energy-0.0.1-SNAPSHOT.jar /app/mayak.energy.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "mayak.energy.jar"]
