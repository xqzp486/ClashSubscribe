# 生成Clash和V2rayN订阅——Java

## [详细的实现细节分析参考本项目的wiki](https://github.com/xqzp486/ClashSubscribe/wiki)

## 一、前提条件
项目使用的框架：Springboot+mybatisPlus<br/>
项目使用的yaml解析工具是Snakeyaml<br/>

项目使用拦截器来拦截请求，然后验证token，验证完token之后，获取token中包含的UUID，查询数据库是否存在该UUID<br/>

项目使用jjwt来签发token,使用时请务必自行更改token的密钥,在JwuUtils里面。

本项目以ws、ws+tls、tcp+tls协议的封装为示范，其他协议需要自己再微调一下

## 二、简介
[具体实现细节分析参考本项目的wiki](https://github.com/xqzp486/ClashSubscribe/wiki)

### 1、Clash订阅

Clash订阅原理是向订阅的地址发送get请求，下载Clash的配置文件——yaml格式<br/>
本文使用的yaml解析工具是Snakeyaml

### 2、V2rayN订阅

V2rayN的订阅原理是向订阅的地址发送get请求,服务器将节点信息封装成json字符串，再对其进行base64加密,再拼接协议头返回

形式1：vmess://(base64节点信息)

形式2：多个vmess://(base64)以换行符进行分割，再进行一次整体的base64加密

以上两种形式可被V2rayN客户端所接受



## 三、项目的实现

验证token信息是否有效，解析token获取uuid，根据uuid查询数据库，封装信息，生成订阅

### 1、数据表
数据表的结构存放在sqlfile.sql文件中

一共三张表，一张是用户表，一张是服务器信息表，因为服务器和用户是多对多的关系，因此我们需要准备第三张表，用户表和服务器信息的映射关系表

### 2、Dockerfile
可用于docker部署项目
注意:如果mysql和项目都在docker容器中，那么项目内配置文件的数据库地址不能为localhost，因为mysql和项目会分配到两个不同的ip地址中，两个地址在同一个网关下。
数据库地址必须填mysql所在的ip，或者指定自定义docker的网络

~~~ docker
docker build -t proxy .

docker run -d -p 8080:8080 \
-v /etc/springboot/config:/config \
-e TZ="Asia/Shanghai" \
--name proxy proxy:1.1
~~~

### 3、SubscribeProject
本项目的代码

## 四、项目细节简述

### 1.项目的几个类介绍

- Server类映射数据库的服务器信息表
- User类映射数据库的用户表
- UserServer类用于映射User和Server关系表
- Proxy代表结点信息，由Server和User类封装而成
- Group代表组信息
- GroupConvert 用途是将Group对象转换成LinkedHashMap，用于封装yaml
- ProxyConvert 用途是将Proxy对象转成LinkedHashMap，用于封装yaml

为什么需要转换成LinkedHashMap，因为Snakeyaml会将读取的yaml文件转换成一系列LinkedHashMap和ArrayList的组合。所以我们必须要把Java对象转换成LinkedHashMap，然后封装进去

### 2、项目的其他功能

（1）ScheduledTask

ScheduledTask是一个定时任务，该任务主要是和wikihost家的转发服务做对接，功能是每天早上9点，解析转发域名的txt记录，获取转发的IP，和数据库的ip做对比，如果发生变动，则更新IP。

如果需要使用的话，请自行在HttpClientUtils的GET请求头中添加自己的x-api-key和x-api-password。在流量转发服务中可以生成，具体参考wikihost家转发的服务API文档

如果无需使用此功能，删除ScheduledTask和HttpClientUtils即可

（2）interceptor

拦截器，拦截请求，验证token和uuid<br/>
AdminWebConfig 将拦截器注册进Spring容器

（3）exception

全局异常处理，不外暴露自己内部的异常信息，仅返回状态码和提示信息<br/>
ExceptionUtil 是用来将异常写进到日志的工具类<br/>
R 自定义的状态响应码

（4）测试类方法

查询数据库的所有用户，获取uuid，然后根据uuid生成token，然后存入数据库