# seeds java

## 项目启动流程
1.准备mysql数据库和数据
2.准备redis数据库
3.将本地hosts文件中添加
54.168.239.215 Seeds-redis
54.168.239.215 Seeds-mysql
54.168.239.215 Seeds-config
注意改成自己的ip

3.首先依次启动seeds-config项目、seeds-gateway
其他不区分顺序seeds-uc、seeds-admin
## JAVA
jdk 1.8
spring cloud version: 2021.0.3 
spring boot version: 2.7.1
mybatis plus version: 3.5.1

## cache: 
redis 6.0.2

## message bus:
kafka 3.2.1

## database
mysql 8.0.2

## swagger
swagger 3.0
访问地址：http://ip:9999/swagger-ui/index.html
增强版本：http://ip:9999/doc.html

## 数据库规范
1.表名称规范
uc端的表名称：uc_xx
admin端的表名称：sys_xx
2.公共字段

