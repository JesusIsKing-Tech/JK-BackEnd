FROM openjdk:17-jdk-alpine
RUN mkdir /app
WORKDIR /app

ENV DB_HOST=44.211.128.210
ENV DB_PORT=3306
ENV DB_NAME=JesusIsKingTech
ENV DB_USERNAME=admin_pibvm
ENV DB_PASSWORD=Pibvm2025!


COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]