FROM openjdk:8-jdk-slim
ADD /build/libs/gateway-0.0.1.jar gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/gateway.jar"]