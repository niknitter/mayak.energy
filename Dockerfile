FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/mayak.energy-0.0.1-SNAPSHOT.jar /app/mayak.energy.jar

ENTRYPOINT ["java", "-jar", "mayak.energy.jar"]
