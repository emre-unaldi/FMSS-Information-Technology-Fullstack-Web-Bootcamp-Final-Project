FROM openjdk:21
LABEL author="emreunaldi <emree.unaldi@gmail.com>"
WORKDIR /photo-service
ADD target/photo-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "photo-service-0.0.1-SNAPSHOT.jar"]