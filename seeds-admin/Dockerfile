FROM openjdk:8-jdk-alpine
MAINTAINER hewei 952517823@qq.com

#captcher 字体包，解决获取图片验证码，报找不到字体空指针
RUN set -xe \
&& apk --no-cache add ttf-dejavu fontconfig

ARG JAR_FILE
COPY ${JAR_FILE} admin.jar
EXPOSE 10102
ENTRYPOINT ["java","-jar","/admin.jar"]