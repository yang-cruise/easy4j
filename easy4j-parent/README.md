## 快速开始
我们将通过一个简单的 Demo 来阐述 easy4j 的强大功能，在此之前，我们假设您已经：


- 拥有 Java 开发环境以及相应 IDE
- 熟悉 Spring Boot
- 熟悉 Maven
- 熟悉 MySQL
- 拥有 Vue 开发环境以及相应 IDE
- 熟悉 Vue


## 初始化工程
创建一个空的 Spring Boot 工程（工程将以 MySQL 作为默认数据库进行演示）

> 可以使用 [Spring Initializer](https://start.spring.io/) 快速初始化一个 Spring Boot 工程


## 添加依赖
引入 Spring Boot Starter 父工程：

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>{latest-version}</version>
    <relativePath/>
</parent>
```
引入  `easy4j-admin-spring-boot-starter`  依赖：

```
<parent>
    <groupId>cn.easy4j</groupId>
    <artifactId>easy4j-admin-spring-boot-starter</artifactId>
    <version>{latest-version}</version>
</parent>
```


## 参数配置
在  `application.yml`  配置文件中添加 MySQL 数据库的相关配置

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/easy4j?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=true&amp;serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```


## 启动后端
运行 `Spring Boot` 项目启动类


## 启动前端
进入`easy4j-admin-ui`文件夹，运行以下命令
```
npm install
npm run dev
```


## 开始体验
现在可以访问 [http://localhost:8080](http://localhost:8080/) 体验 `easy4j` 为你提供的基础功能了。