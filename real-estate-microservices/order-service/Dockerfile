FROM openjdk:21
LABEL author="emreunaldi <emree.unaldi@gmail.com>"
WORKDIR /order-service
ADD target/order-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]