FROM openjdk:21
LABEL author="emreunaldi <emree.unaldi@gmail.com>"
WORKDIR /advert-service
ADD target/advert-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "advert-service-0.0.1-SNAPSHOT.jar"]