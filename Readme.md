# 生成Clash和V2rayN订阅——Java

## [详细的实现细节分析参考本项目的wiki](https://github.com/xqzp486/ClashSubscribe/wiki)

## 一、前提条件
项目使用的框架：Springboot+MybatisPlus<br/>
项目使用的yaml解析工具是Snakeyaml<br/>

项目使用拦截器来拦截请求，然后验证token，验证完token之后，获取token中包含的UUID，查询数据库是否存在该UUID<br/>

项目使用jjwt来签发token,使用时请务必自行更改token的密钥,在配置文件中

本项目目前只支持vmess+ws、vmess+ws+tls、vmess+tcp+tls、vless+tcp+tls+vless+xtls-rprx-origin

生成Clash订阅时会自动过滤掉vless结点，因为一般的Clash内核不支持vless

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

> 注意事项
>
> 其中vless协议的格式，仅支持一次base64加密
>
> vmess后面的base64信息是json字符串的封装，而vless的信息不同，直接是字符串的拼接
>
> 示例如下
>
> vless://UUID@IP:PORT?encryption=none&flow=xtls-rprx-direct&security=tls&type=tcp&headerType=none#NAME



## 三、项目的实现

验证token信息是否有效，解析token获取uuid，根据uuid查询数据库，封装信息，生成订阅

### 1、数据表
数据表的结构存放在sqlfile.sql文件中

一共三张表，一张是用户表，一张是服务器信息表，因为服务器和用户是多对多的关系，因此我们需要准备第三张表，用户表和服务器信息的映射关系表

token字段无需自己填充，项目启动时，会自行根据uuid，生成初始的token

### 2、Dockerfile
可用于docker部署项目
注意:如果mysql和项目都在docker容器中，那么项目内配置文件的数据库地址不能为localhost，因为mysql和项目会分配到两个不同的ip地址中，两个地址在同一个网关下。
数据库地址必须填mysql所在的ip，或者指定自定义docker的网络

~~~ docker
docker build -t proxy:1.0.1 .

docker run -d -p 8080:8080 \
-v /etc/springboot/config:/config \
-v /var/log/myproxy:/var/log/myproxy \
-e TZ="Asia/Shanghai" \
--name proxy proxy:1.0.1
~~~

### 3、SubscribeProject
本项目的代码

## 四、项目细节简述

### 1.项目的几个类介绍

- Server类、User类、UserServer类分别映射数据库的服务器信息表、用户表和用户服务器关系表
- 使用工厂模式生成v2ray的vmess和vless订阅信息
- Proxy代表结点信息，由Server和User类封装而成
- Group代表组信息
- GroupConvert 用途是将Group对象转换成LinkedHashMap，用于封装yaml
- ProxyConvert 用途是将Proxy对象转成LinkedHashMap，用于封装yaml

为什么需要转换成LinkedHashMap？

因为Snakeyaml会将读取的yaml文件转换成一系列LinkedHashMap和ArrayList的组合。所以我们必须要把Java对象转换成LinkedHashMap，然后封装进去

### 2、项目的其他功能

（1）ScheduledTask

ScheduledTask是一个定时任务，该任务主要是和wikihost家的转发服务做对接，功能是定时解析转发域名的txt记录，获取转发的IP，和数据库的ip做对比，如果发生变动，则更新IP

如果想要使用的话，需要在配置文件中添加自己的x-api-key和x-api-password。并删除删除ScheduledTask上的注释，将组件添加进容器。并修改具体的corn表达式

x-api-key和x-api-password的生成，具体参考wikihost家转发的服务API文档

（2）interceptor

拦截器，拦截请求，验证token和uuid<br/>
AdminWebConfig 将拦截器注册进Spring容器

（3）exception

全局异常处理，不外暴露自己内部的异常信息，仅返回状态码和提示信息<br/>
ExceptionUtil 是用来将异常写进到日志的工具类<br/>
R 自定义的状态响应码

（4）listener

项目启动时，会检查所有用户的token，是否存在，是否过期

如果用户token不存在或者已经过期，则根据用户的uuid生成新的token存入数据库