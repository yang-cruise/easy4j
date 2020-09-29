# Easy4J

## 简介
`Easy4J` 是在 `Spring Boot 2.x` 基础上开发的脚手架，你可以单独依赖Java后端项目，也可以配合前端项目一起快速搭建一个后台管理系统。

开发文档：[www.easy4j.cn](https://www.easy4j.cn)

演示系统：[admin.easy4j.cn](https://admin.easy4j.cn)    admin/123456



## 愿景
我们的愿景是让Java开发更简单，就像项目名称一样：`Easy for Java.`



## 最新版本

```xml
<dependency>
    <groupId>cn.easy4j</groupId>
    <artifactId>easy4j-spring-boot-starter</artifactId>
    <version>0.5.0.RELEASE</version>
</dependency>
```



## 项目结构

```javascript
|- easy4j
    |- easy4j-sample  // 示例项目
    |- easy4j-parent  // easy4j父项目
        |- easy4j-common  // 公共模块，存放公共常量、异常、枚举等。
        |- easy4j-spring-boot-starter  // 核心模块，提供基础功能
        |- easy4j-admin-spring-boot-starter  // 后台管理模块，是easy4j-admin-ui前端项目的服务端
        |- easy4j-dict-spring-boot-starter  // 数据字典模块
        |- easy4j-oss-spring-boot-starter  // 文件存储模块
```



## 快速开始
以下步骤将带你快速搭建一个 `Easy4J` 的项目，你也可以直接查看 [示例项目](https://github.com/yang-cruise/easy4j/tree/master/easy4j-sample)

### 1. 初始化工程
创建一个空的 Spring Boot 工程

> 可以使用 [Spring Initializer](https://start.spring.io/) 快速初始化一个 Spring Boot 工程

### 2. 添加依赖
引入 Spring Boot Starter 父工程：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>${spring-boot.latest.version}</version>
</parent>
```

引入  `easy4j-admin-spring-boot-starter`  依赖：
```xml
<dependency>
    <groupId>cn.easy4j</groupId>
    <artifactId>easy4j-admin-spring-boot-starter</artifactId>
    <version>${easy4j.latest.version}</version>
</dependency>
```

### 3. 创建数据库
在MySQL数据库中创建一个空的 `database`

### 4. 数据源配置
在  `application.yml`  配置文件中添加MySQL数据库的相关配置

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/easy4j?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=true&amp;serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 5. 启动后端
运行 `Spring Boot` 项目启动类

### 6. 启动前端
下载 [easy4j-admin-ui](https://github.com/yang-cruise/easy4j-admin-ui) 项目，运行以下命令
```
npm install
npm run dev
```

### 7. 开始体验
现在可以访问 [本地环境](http://localhost:8080/) 体验 `Easy4J` 为你提供的基础功能了