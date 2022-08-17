FROM openjdk:8-jdk-alpine
MAINTAINER hewei 952517823@qq.com
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8889
ENTRYPOINT ["java","-jar","/app.jar"]