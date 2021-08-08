# 第 03 节：跑通广播模式RPC过程调用

<div align="center">
    <img src="https://codechina.csdn.net/KnowledgePlanet/Lottery/-/raw/master/doc/assets/img/3-00.png" width="128">
</div>

- 分支：[210804_xfg_buildFramework](https://codechina.csdn.net/KnowledgePlanet/Lottery/-/tree/210804_xfg_buildFramework)
- 描述：构建工程完成RPC接口的实现和调用

当基础的工程模块创建完成以后，还需要给整个工程注入`灵魂`，就是让它可以跑通。这个过程包括一个简单的 RPC 接口功能实现和测试调用，那么这里为了让功能体现出一个完整度，还会创建出一个库表在 RPC 调用的时候查询出库表中的数据并🔙返回结果。那么在这个分支上我们就先来完成这样一个内容的实现。

## 一、创建抽奖活动表

在抽奖活动的设计和开发过程中，会涉及到的表信息包括：活动表、奖品表、策略表、规则表、用户参与表、中奖信息表等，这些都会在我们随着开发抽奖的过程中不断的添加出来这些表的创建。

那么目前我们为了先把程序跑通，可以先简单的创建出一个活动表，用于实现系统对数据库的CRUD操作，也就可以被RPC接口调用。在后面陆续实现的过程中可能会有一些不断优化和调整的点，用于满足系统对需求功能的实现。

**活动表(activity)**

| 字段          | 类型         | 描述                                                     |
| ------------- | ------------ | -------------------------------------------------------- |
| id            | bigint(20)   | 自增ID                                                   |
| activityId    | bigint(20)   | 活动ID                                                   |
| activityName  | varchar(64)  | 活动名称                                                 |
| activityDesc  | varchar(128) | 活动描述                                                 |
| beginDateTime | datetime     | 开始时间                                                 |
| endDateTime   | datetime     | 结束时间                                                 |
| stockAllTotal | int(11)      | 库存(总)                                                 |
| stockDayTotal | int(11)      | 库存(日)                                                 |
| takeAllCount  | int(11)      | 每人可参与次数(总)                                       |
| takeDayCount  | int(11)      | 每人可参与次数(日)                                       |
| state         | int(4)       | 活动状态：编辑、提审、撤审、通过、运行、拒绝、关闭、开启 |
| creator       | varchar(64)  | 创建人                                                   |
| createTime    | datetime     | 创建时间                                                 |
| updateTime    | datetime     | 修改时间                                                 |

- 活动表：是一个用于配置抽奖活动的总表，用于存放活动信息，包括：ID、名称、描述、时间、库存、参与次数等。

## 二、POM 文件配置

按照现有工程的结构模块分层，包括：
- lottery-application，应用层，引用：`domain`
- lottery-common，通用包，引用：`无`
- lottery-domain，领域层，引用：`infrastructure`
- lottery-infrastructure，基础层，引用：`无`
- lottery-interfaces，接口层，引用：`application`、`rpc`
- lottery-rpc，RPC接口定义层，引用：`common`

在此分层结构和依赖引用下，各层级模块不能循环依赖，同时 `lottery-interfaces` 作为系统的 war 包工程，在构建工程时候需要依赖于 POM 中配置的相关信息。那这里就需要注意下，作为 Lottery 工程下的主 pom.xml 需要完成对 SpringBoot 父文件的依赖，此外还需要定义一些用于其他模块可以引入的配置信息，比如：jdk版本、编码方式等。而其他层在依赖于工程总 pom.xml 后还需要配置自己的信息。

### 1. 总工程下POM配置

```xml
<properties>
    <!-- Base -->
    <jdk.version>1.8</jdk.version>
    <sourceEncoding>UTF-8</sourceEncoding>
</properties>

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.5.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

- 相对于工程下其他的模块来说，总POM就是这些模块的父类模块，在父类模块中一般只提供基础的定义，不提供各个Jar包的引入配置。如果在父类 POM 中引入了所有的 Jar，那么各个模块无论是否需要这个 Jar 都会被自动引入进去，造成没必要的配置，也会影响对核心Jar的扰乱，让你分不清自己需要的是否就在眼前。

### 2. 模块类POM配置

```xml
<parent>
    <artifactId>Lottery</artifactId>
    <groupId>cn.itedus.lottery</groupId>
    <version>1.0-SNAPSHOT</version>
</parent>
<modelVersion>4.0.0</modelVersion>
<artifactId>lottery-rpc</artifactId>

<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>cn.itedus.lottery</groupId>
        <artifactId>lottery-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<build>
    <finalName>lottery-rpc</finalName>
    <plugins>
        <!-- 编译plugin -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>${jdk.version}</source>
                <target>${jdk.version}</target>
                <compilerVersion>1.8</compilerVersion>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- 在各个模块配置中需要关注点包括：依赖父POM配置`parent`、构建包类型`packaging`、需要引入的包`dependencies`、构建信息`build`之所以要配置这个是有些时候在这个模块工程下还可能会有一些差异化信息的引入。

### 3. War包pom配置

```xml
<artifactId>lottery-interfaces</artifactId>

<packaging>war</packaging>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ...
</dependencies>

<build>
    <finalName>Lottery</finalName>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/**</include>
            </includes>
        </resource>
    </resources>
    <testResources>
        <testResource>
            <directory>src/test/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/**</include>
            </includes>
        </testResource>
    </testResources>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>8</source>
                <target>8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- lottery-interfaces 是整个程序的出口，也是用于构建 War 包的工程模块，所以你会看到一个 `<packaging>war</packaging>` 的配置。
- 在 dependencies 会包含所有需要用到的 SpringBoot 配置，也会包括对其他各个模块的引入。
- 在 build 构建配置上还会看到一些关于测试包的处理，比如这里包括了资源的引入也可以包括构建时候跳过测试包的配置。

## 三、配置Mybatis

SpringBoot 与 Mybatis 的结合使用还是非常方便的，暂时我们也还没有引入分库分表路由的组件，所以只是一个引入操作数据库的简单配置。

**引入 starter**

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.5-SNAPSHOT</version>
</dependency>
```

- 在 SpringBoot 的使用中，你会看到各种 xxx-starter，它们这些组件的包装都是用于完成桥梁的作用，把一些服务交给 SpringBoot 启动时候初始化或者加载配置等操作。

**配置 yml**

```xml
spring:
  datasource:
    username: root
    password: 1234
    url: jdbc:mysql://127.0.0.1:3306/lottery?useUnicode=true
    driver-class-name: com.mysql.jdbc.Driver

mybatis:
  mapper-locations: classpath:/mybatis/mapper/*.xml
  config-location:  classpath:/mybatis/config/mybatis-config.xml
```

- 配置 Spring.datasource 数据源和mybatis文件的引入配置，这些mybatis文件主要的是包括了各个 mapper 的处理。*当然你也可以习惯于使用注解的方式使用数据库*
- 如果是引入了分库分表组件，那么这里就需要配置多个数据源信息，在后面的开发过程中会有所体现。

## 四、配置广播模式 Dubbo

首先说说为什么要配置广播模式的 Dubbo，其实最早 RPC 的设计和使用都是依赖于注册中心，那就是需要把服务接口信息在程序启动的时候，推送到一个统一的注册中心，在其他需要调用 RPC 接口的服务上再通过注册中心的均衡算法来匹配可以连接的接口落到本地保存和更新。那么这样的标准的使用方式可以提供更大的连接数和更强的负载均衡作用，但目前我们这个以学习实践为目的的工程开发则需要尽可能减少学习成本，也就需要在开发阶段少一些引入一些额外的配置，那么目前使用广播模式就非常适合，以后也可以直接把 Dubbo 配置成注册中心模式。官网：[https://dubbo.apache.org](https://dubbo.apache.org/zh/docs/quick-start/)

**配置 yml**

```xml
# Dubbo 广播方式配置
dubbo:
  application:
    name: Lottery
    version: 1.0.0
  registry:
    address: multicast://224.5.6.7:1234
  protocol:
    name: dubbo
    port: 20880
  scan:
    base-packages: cn.itedus.lottery.rpc
```

- 广播模式的配置唯一区别在于注册地址，`registry.address = multicast://224.5.6.7:1234`，服务提供者和服务调用者都需要配置相同的📢广播地址。
- application，配置应用名称和版本
- protocol，配置的通信协议和端口
- scan，相当于 Spring 中自动扫描包的地址，可以把此包下的所有 rpc 接口都注册到服务中

## 五、定义和开发 RPC 接口

由于 RPC 接口在通信的过程中，需要提供接口的描述文件，也就是接口的定义信息。所以这里你会看到我们会把所有的 RPC 接口定义都放到 `lottery-rpc` 模块下，这种方式的使用让外部就只依赖这样一个 pom 配置引入的 Jar 包即可。

**定义接口**

![](https://codechina.csdn.net/KnowledgePlanet/Lottery/-/raw/master/doc/assets/img/3-01.png)

```java
public interface IActivityBooth {

    ActivityRes queryActivityById(ActivityReq req);

}
```

- 这里先来定义一个`活动展台`的接口类，用于包装活动的创建、查询、修改、审核的接口。

**开发接口**

```java
@Service
public class ActivityBooth implements IActivityBooth {

    @Resource
    private IActivityDao activityDao;

    @Override
    public ActivityRes queryActivityById(ActivityReq req) {

        Activity activity = activityDao.queryActivityById(req.getActivityId());

        ActivityDto activityDto = new ActivityDto();
        activityDto.setActivityId(activity.getActivityId());
        activityDto.setActivityName(activity.getActivityName());
        activityDto.setActivityDesc(activity.getActivityDesc());
        activityDto.setBeginDateTime(activity.getBeginDateTime());
        activityDto.setEndDateTime(activity.getEndDateTime());
        activityDto.setStockAllTotal(activity.getStockAllTotal());
        activityDto.setStockDayTotal(activity.getStockDayTotal());
        activityDto.setTakeAllCount(activity.getStockAllTotal());
        activityDto.setTakeDayCount(activity.getStockDayTotal());

        return new ActivityRes(new Result(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo()), activityDto);
    }

}
```

- 用于实现 RPC 接口的实现类 ActivityBooth 上有一个注解 @Service，这个注解是来自于 Dubbo 的 `org.apache.dubbo.config.annotation.Service`，也就是这个包下含有此注解配置的类可以被 Dubbo 管理。
- 在 queryActivityById 功能实现中目前还比较粗糙，但大体可以看出这是对数据库的操作以及对结果的封装，提供 DTO 的对象并返回 Res 结果。*目前dto的创建后续可以使用门面模式和工具类进行处理*

**以上就是当前工程对 RPC 接口的一个使用流程的实现，后续会在此基础上添加各个模块的功能。**

## 六、搭建测试工程调用 RPC

为了测试 RPC 接口的调用以及后续其他逻辑的验证，这里需要创建一个测试工程：[Lottery-Test](https://codechina.csdn.net/KnowledgePlanet/Lottery-Test) 这个工程中用于引入 RPC 接口的配置和同样广播模式的调用。

### 1. 配置 POM 

```xml
<dependency>
    <groupId>cn.itedus.lottery</groupId>
    <artifactId>lottery-rpc</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

- 测试之前需要把 Lottery 工程中的 lottery-rpc 进行构建打包，便于测试工程引入

### 2. 配置广播模式 Dubbo

```xml
server:
  port: 8081

# Dubbo 广播方式配置
dubbo:
  application:
    name: Lottery
    version: 1.0.0
  registry:
    address: multicast://224.5.6.7:1234
  protocol:
    name: dubbo
    port: 20880
```

- 这里的配置与 Dubbo 接口提供者是一样的，都需要在一个广播地址下使用。

### 3. 单元测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Reference(interfaceClass = IActivityBooth.class)
    private IActivityBooth activityBooth;

    @Test
    public void test_rpc() {
        ActivityReq req = new ActivityReq();
        req.setActivityId(100001L);
        ActivityRes result = activityBooth.queryActivityById(req);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

}
```

- IActivityBooth 是 RPC 接口，通过 Dubbo 的注解 `@Reference` 进行注入配置。有了这个注解的配置其实调用上就没有太多不同了。

**测试结果**

```java
2021-08-08 12:07:34.898  INFO 9474 --- [           main] cn.itedus.lottery.test.ApiTest           : 测试结果：{"activity":{"activityDesc":"傅哥的活动","activityId":100001,"activityName":"测试活动","beginDateTime":1628061494000,"endDateTime":1628061494000,"stockAllTotal":100,"stockDayTotal":10,"takeAllCount":100,"takeDayCount":10},"result":{"code":"0000","info":"成功"}}
2021-08-08 12:07:34.915  INFO 9474 --- [extShutdownHook] .b.c.e.AwaitingNonWebApplicationListener :  [Dubbo] Current Spring Boot Application is about to shutdown...
```

- 通过测试结果可以看到，目前通过 RPC 调用已经可以查询到分布式系统提供的数据库操作功能。

## 七、本章知识点

- DDD + RPC 各个分层模块的 POM 配置和依赖关系
- Mybatis 的配置和使用
- Dubbo 中广播模式的配置，在你实际使用的过程中一般都是使用注册中心模式