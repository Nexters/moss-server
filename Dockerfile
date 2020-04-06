FROM openjdk:8-jdk-alpine
COPY ./build/libs/*.jar ./habikery-api.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./habikery-api.jar"]