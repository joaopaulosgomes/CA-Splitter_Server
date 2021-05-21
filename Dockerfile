FROM openjdk:latest

COPY build/libs/ca-splittr-0.0.1-SNAPSHOT.jar /app/ca-splittr-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/ca-splittr-0.0.1-SNAPSHOT.jar"]