FROM openjdk:21
LABEL author="emreunaldi <emree.unaldi@gmail.com>"
WORKDIR /auth-service
ADD target/auth-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "auth-service-0.0.1-SNAPSHOT.jar"]