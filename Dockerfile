FROM java:openjdk-8-alpine
ADD /build/libs/gateway-0.0.1.jar gateway.jar
ENTRYPOINT ["java","-jar","gateway.jar"]