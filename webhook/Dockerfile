FROM openjdk:8-jre
MAINTAINER Daniel Felizi <felizi.daniel@gmail.com>

ADD ./target/webhook.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/webhook.jar"]

EXPOSE 8080