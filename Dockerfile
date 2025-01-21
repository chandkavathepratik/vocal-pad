FROM openjdk:22-jdk-slim

WORKDIR /vocalPad

COPY target/vocalPad-0.0.1-SNAPSHOT.jar vocalPad.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "vocalPad.jar"]