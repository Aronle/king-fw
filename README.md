# king-fw

#### 介绍
King-fw 是基于spring-boot和spring-cloud的底层应用框架，把spring-boot和spring-cloud的配置和依赖隔离开来可以灵活配置。可以自己维护这个基础框架；在FW-BOOT和FW-CLOUD灵活选择，新增删减配置。FW-CORE框架里面包含了一个spring-boot和spring-cloud型应用的基础功能封装。具体架构在下面会介绍，框架旨在为业务应用提供一套可快速屏蔽底层实现的应用型框架。该框架具备业务开发的基础功能和实现，使用者不用关心spring-boot和spring-cloud的基础配置。应用只需要引入，加上少许配置就可以进行业务开发。
#### 软件架构
软件架构说明
![输入图片说明](https://images.gitee.com/uploads/images/2020/0514/155805_e5bbd749_994798.png "屏幕截图.png")

#### 安装教程

我把这个框架生态分成三大块：核心模块，功能集成模块，服务性模块。应用程序只要引入core模块就可以具备spring-boot和spring-cloud的基础功能和框架提供的基础封装功能了，详细的功能可以看图或者源码。我一直认为好的框架一定是简单，适用，演进的。 我实践过很多把core这个拆分成好多个jar，看似拆的很细，逼格很高但是实际对真实的业务并不友好，作为业务方对这样的基础功能你给一个包就好了。做的越少犯错也就越少，但并不是牺牲框架的能力为代价，这是一个很自然的过程，有时候我们总是想的太复杂，过度设计，抛开业务去谈框架是耍流氓的。

1.核心模块：core首先要具备的功能是spring-boot和spring-cloud，其次是我们平常开发一个应用都要干的事（后面章节展开来讲）。

2.功能集成模块：开发应用通常要具备的功能，但不是每个应用都需求具备的功能（后面章节展开来讲）。

3.服务模块:  开应用可能需求依赖的服务，但不是自己具备的或者说依赖他人具备是更好的事（后面章节展开来讲）。

1.FW-BOOT:  是不会对外提供的只是用来管理spring-boot的依赖和配置用的,会被core依赖，这样对框架开发维护人员是比较好的

2.FW-CLOUD: 是不会对外提供的只是用来管理spring-cloud的依赖和配置用的，会被core依赖，这样对框架开发维护人员是比较好的。

3.FW-CORE: 主要分成几大块：

core-web:  对web服务api做了封装提供了web应用程序都会用到基础功能，比如：统一返回数据格式，统一异常，统一检验，在文档，统一上下文封装，统一参数解析等，应用程序引入后就不必关注这些，可以专注于业务。可扩展

core-mybastis:  对DB层的封装  可扩展

core-log： 统一日志输出封装

core-encrypt:  加解密封装，可扩展

core-common:  基础常量，基础实体，基础DAO等，包括应用程序基础工具类 可扩展

core-cache: 基础缓存工具类提供，可扩展

4.FW-SECURITY:  集成模块，基于spring-security，提供非微服务的认证授权功能。已包含基础表设计（后续完善）。应用程序只要依赖集成这个模块就能拥有基本的认证授权功能。认证包括了（账号密码，手机邮箱密码，三方（微信，微博，QQ）登录）。使用者可以下载源码自行扩展定义满足自己团队公司的认证授权方式。

5.FW-WEB: 框架服务测试web模块，示例了如何使用core框架和集成模块。


#### 使用说明

##### 不需要spring-cloud 使用示例：FW-NOCLOUD-WEB 
1.新建一个maven的项目包命的前两级使用com.mars
2.在com.mars下新建一个启动主函数类使用：@SpringBootApplication注解
3.在pom中引入：FW-SECURITY 根据需要引入

```
 <dependency>
            <groupId>com.mars.fw</groupId>
            <artifactId>FW-CORE</artifactId>
            <version>1.0.0-SNAPSHOT</version>
             <exclusions>
                <exclusion>
                    <groupId>com.mars.fw</groupId>
                    <artifactId>FW-CLOUD</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--有需要引入-->
        <dependency>
            <groupId>com.mars.fw</groupId>
            <artifactId>FW-SECURITY</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
</dependencies>
```
4.在resources下新建一个appliction.yml文件做如下必要配置

```
server:
  port: 10001
spring:
  liquibase:
    enabled: false
  redis:
    database: 11
    host: localhost
    jedis:
      pool:
        max-idle: 300
        max-wait: 30s
    port: 6379
  datasource:
    url: jdbc:mysql://172.16.6.101/king?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8
    username: vvoadatauser
    password: Heikfh*344)$%bve
    driver-class-name: com.mysql.cj.jdbc.Driver
    #连接池配置
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      stat-view-servlet:
        allow: 127.0.0.1
        login-password: admin
        login-username: admin
        enabled: true
        url-pattern: /druid/*
      initial-size: 5
      max-active: 100
      max-wait: 60000
      time-between-eviction-runs-millis: 30000
      min-evictable-idle-time-millis: 60000
      validation-query: SELECT 1 FROM DUAL
      filters: stat
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=3000;${db.encryption}
      connection-init-sqls: set names utf8mb4;
      web-stat-filter:
        enabled: true
mybatis-plus:
  mapper-locations: classpath*:com.mars.fw.web.mapper/*Mapper.xml
```
5.如果是在外部使用，先要把框架FW-CORE发布到自己的NEXUS，后续我会提供这个我自己私服发布的maven依赖
  
##### 微服务应用示例：FW-WEB
1.微服务的使用，首先要安装nacos，安装nacos后我们的注册和服务发现都在上面管理：
![nacos](https://images.gitee.com/uploads/images/2020/0519/133428_deb2434e_994798.png "屏幕截图.png")
2.同样的新建一个maven的空项目这边以FW-WEB来做示例，后续会搭建更完整的业务项目结构来介绍（推荐版目录）
3.在com.mars下新建一个启动主函数类:FwWebApplication 这边使用 @SpringCloudApplication注解

```
package com.mars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @Author King
 * @create 2020/4/20 12:22
 */
@Slf4j
@SpringCloudApplication
public class FwWebApplication {

    public static void main(String[] args) {
        String name = "King-gateway";
        try {
            Yaml yaml = new Yaml();
            URL bootstrap = FwWebApplication.class.getClassLoader().getResource("bootstrap.yml");
            if (bootstrap != null) {
                InputStream resourceAsStream = FwWebApplication.class.getClassLoader().getResourceAsStream("bootstrap.yml");
                Map map = (Map) yaml.load(resourceAsStream);
                Map spring = (Map) map.get("spring");
                Map application = (Map) spring.get("application");
                name = (String) application.get("name");
            }
            log.error("服务以{}启动", name);
            SpringApplication.run(FwWebApplication.class, args);
        } catch (Exception e) {
            log.error("[" + name + "]启动异常", e);
            System.exit(1);
        }
    }

}

```

5.在pom中引入：FW-SECURITY 根据需要引入

```
 <dependency>
            <groupId>com.mars.fw</groupId>
            <artifactId>FW-CORE</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <!--有需要引入-->
        <dependency>
            <groupId>com.mars.fw</groupId>
            <artifactId>FW-SECURITY</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
</dependencies>
```
6.和单体应用还有一个区别在于：在微服务中我们在代码中使用bootstrap.yml作为配置启动入口，真正的配置文件我们放在nacos中做管理

```
spring:
  application:
    name: ${application.name}
  cloud:
    nacos:
      config:
        server-addr: ${nacos.config.address}
        shared-dataids: ${spring.application.name}.yml
        refreshable-dataids:  ${spring.application.name}.yml
        namespace: ${config.namespace}
      discovery:
        server-addr: ${nacos.discovery.address}
        namespace: ${discovery.namespace}
        register-enabled: true
  main:
    allow-bean-definition-overriding: true
```



#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

