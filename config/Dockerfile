FROM openjdk:8-jre
MAINTAINER Daniel Felizi <felizi.daniel@gmail.com>

ADD ./target/config.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/config.jar"]

EXPOSE 8888