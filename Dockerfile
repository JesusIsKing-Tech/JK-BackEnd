FROM openjdk:17-jdk-alpine
RUN mkdir /app
WORKDIR /app

ARG EMAIL_SENHA
ARG DB_HOST

ENV DB_HOST=${DB_HOST}
ENV DB_PORT=3306
ENV DB_NAME=JesusIsKingTech
ENV DB_USERNAME=admin_pibvm
ENV DB_PASSWORD=Pibvm2025!

ENV EMAIL_EMAIL=jesuskingtech@gmail.com
ENV EMAIL_SENHA=${EMAIL_SENHA}

COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]