FROM openjdk:8-jre
MAINTAINER Daniel Felizi <felizi.daniel@gmail.com>

ADD ./target/registry.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/registry.jar"]

EXPOSE 8761