FROM openjdk:8-jre
MAINTAINER Daniel Felizi <felizi.daniel@gmail.com>

ADD ./target/gateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]

EXPOSE 8888