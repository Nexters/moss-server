FROM openjdk:8-jdk-alpine
COPY ./build/libs/*.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=develop,init", "-jar","./app.jar"]