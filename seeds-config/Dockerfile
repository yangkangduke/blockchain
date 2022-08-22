FROM openjdk:8-jdk-alpine
MAINTAINER hewei 952517823@qq.com
ARG JAR_FILE
COPY ${JAR_FILE} config.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/config.jar"]