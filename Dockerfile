FROM openjdk:8-jre-alpine
ADD /build/libs/gateway-0.0.1.jar gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/gateway.jar"]