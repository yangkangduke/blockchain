FROM openjdk:8-jdk-alpine
MAINTAINER hewei 952517823@qq.com
ARG JAR_FILE
COPY ${JAR_FILE} uc.jar
EXPOSE 10101
ENTRYPOINT ["java","-jar","/uc.jar"]