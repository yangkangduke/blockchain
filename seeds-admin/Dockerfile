FROM java:8
MAINTAINER hewei 952517823@qq.com
ARG JAR_FILE
COPY ${JAR_FILE} admin.jar
EXPOSE 10102
ENTRYPOINT ["java","-jar","/admin.jar"]