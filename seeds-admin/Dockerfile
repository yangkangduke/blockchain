FROM openjdk:8-jdk-alpine
MAINTAINER hewei 952517823@qq.com

#captcher 字体包，解决获取图片验证码，报找不到字体空指针
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

ARG JAR_FILE
COPY ${JAR_FILE} admin.jar
EXPOSE 10102
ENTRYPOINT ["java","-jar","/admin.jar"]