# 生活服务类网站 #

## 一、项目环境准备 ##
1.本地服务器配置，配置Tomcat8.5。

2.JDK1.8

3.Maven3.3.9

4.添加Tomcat8.5相关Library。

5.修改pom.xml，添加maven-compiler-plugin相关依赖。


    <build>
		<finalName>o2o</finalName>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.6.0</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<encoding>UTF8</encoding>
				</configuration>
			</plugin>
		</plugins>
	</build>

6.JavaWeb类型主要分为静态和动态类型。

<?xml version="1.0" encoding="UTF-8"?>
<faceted-project>
  <fixed facet="wst.jsdt.web"/>
  <installed facet="jst.web" version="**3.1**"/>
  <installed facet="wst.jsdt.web" version="1.0"/>
  <installed facet="java" version="1.8"/>
</faceted-project>

修改web.xml

    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	version="3.1" metadata-complete="true">

7.将项目添加到tomcat，在运行。

设置默认访问页面

    <display-name>Archetype Created Web Application</display-name>
      <welcome-file-list>
      <welcome-file>index.jsp</welcome-file>
      <welcome-file>index.html</welcome-file>
  `</welcome-file-list>`

# 二、项目设计以及系统架构 #

## 1.前端系统展示：

 ##1.轮播主页展示，2.店铺类别展示 3.区域展示

4.店铺：列表展示，查询，详情；5.商品：列表展示，查询，详情。

## 2.店家系统： 

##1.本地账号维护，2.微信账号维护，3.权限验证，4.店铺信息维护 5.商品类别维护。

## 3.超级管理员系统 ##

1.轮播主页信息维护 2.店铺类别信息维护 3.权限验证 4.区域信息维护，5.店铺管理，6.用户管理。

## 4.对象设计 ##

实体类解析：

1.区域；2.用户信息：微信账号，用户店家登录，本地账号；3.主页推荐展示；4.店铺类别；5.店铺6.商品：详情图片7.商品类别。

### 实体类与对应数据库表设计 

**1.区域**：ID，权重：排序优先级，创建时间，修改时间，名称。

在entity中创建Area区域类，并创建相应表tb_area。

    CREATE TABLE `tb_area` (
      `area_id` int(5) NOT NULL AUTO_INCREMENT,
      `area_name` varchar(200) NOT NULL,
      `area_desc` varchar(1000) DEFAULT NULL,
      `priority` int(2) NOT NULL DEFAULT '0',
      `create_time` datetime DEFAULT NULL,
      `last_edit_time` datetime DEFAULT NULL,
      PRIMARY KEY (`area_id`),
      UNIQUE KEY `UK_AREA` (`area_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

指定主键(area_id)和唯一键(area_name)，指定数据表的引擎为InnoDB，主键唯一递增。

区别：InnoDB是基于行级锁，MYISAM是表级锁，由于涉及到某些行数据的并发修改，所以只能选用行级锁。

**2.用户信息**：ID，姓名，头像，邮箱，修改时间，创建时间，性别，状态：是否有进入权限，身份标识：店家，用户，超级管理员。

在entity中创建PersonInfo区域类，并创建相应表tb_person_info。

    CREATE TABLE `tb_person_info` (
      `user_id` int(10) NOT NULL AUTO_INCREMENT,
      `name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
      `gender` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
      `email` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
      `profile_img` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
      `user_type` int(2) NOT NULL DEFAULT '1' COMMENT '1：顾客，2：店家，3：超级管理员。
      `create_time` datetime DEFAULT NULL,
      `last_edit_time` datetime DEFAULT NULL,
      `enable_status` int(2) NOT NULL DEFAULT '0',
      PRIMARY KEY (`user_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
    
**3.微信账号**：ID，用户ID，创建时间，openID，与用户信息表相关联。
在entity中创建WechatAuth区域类，并创建相应表tb_wechat_auth。

    CREATE TABLE `tb_wechat_auth` (
      `wechat_auth_id` INT(10) NOT NULL AUTO_INCREMENT,
      `user_id` INT(10) NOT NULL,
      `open_id` VARCHAR(512) COLLATE utf8_unicode_ci NOT NULL,
      `create_time` DATETIME DEFAULT NULL,
      PRIMARY KEY (`wechat_auth_id`),
      CONSTRAINT `fk_wechatauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
    ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

增加一个外键约束， `CONSTRAINT `fk_wechatauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)`，使得微信账号与用户信息通过user_id相关联。

为open_id建立一个唯一索引，这里当open_id设置为512会报错：

    Query : alter table tb_wechat_auth add unique index(open_id)
    Error Code : 1071
    Specified key was too long; max key length is 767 bytes

将open_id的长度改为128就好了。

    ALTER TABLE tb_wechat_auth ADD UNIQUE INDEX(open_id);


**4.本地账号**：ID，用户名，密码，创建时间，用户ID。

在entity中创建PersonInfo区域类，并创建相应表tb_person_info。

    CREATE TABLE `tb_local_auth` (
      `local_auth_id` INT(10) NOT NULL AUTO_INCREMENT,
      `user_id` INT(10) DEFAULT NULL,
      `user_name` VARCHAR(128) COLLATE utf8_unicode_ci NOT NULL,
      `password` VARCHAR(128) COLLATE utf8_unicode_ci NOT NULL,
      `create_time` DATETIME DEFAULT NULL,
      `last_edit_time` DATETIME DEFAULT NULL,
      PRIMARY KEY (`local_auth_id`),
      UNIQUE KEY `uk_local_profile` (`user_name`),
      CONSTRAINT `fk_local_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
    ) ENGINE=INNODB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
将用户名字设置为唯一键，表示该账号的唯一性，同时增加外键约束，使得本地账号与用户信息通过user_id相关联。

**5.主页轮播类**：ID，权重，状态，修改时间，创建时间，图片，链接，名称。

    CREATE TABLE `tb_head_line` (
      `line_id` int(100) NOT NULL AUTO_INCREMENT,
      `line_name` varchar(1000) DEFAULT NULL,
      `line_link` varchar(2000) NOT NULL,
      `line_img` varchar(2000) NOT NULL,
      `priority` int(2) DEFAULT NULL,
      `enable_status` int(2) NOT NULL DEFAULT '0',
      `create_time` datetime DEFAULT NULL,
      `last_edit_time` datetime DEFAULT NULL,
      PRIMARY KEY (`line_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;


**6.店铺类别**：ID，权重，上级ID，店铺名称，创建时间，修改时间，描述，图片

    CREATE TABLE `tb_shop_category` (
      `shop_category_id` INT(11) NOT NULL AUTO_INCREMENT,
      `shop_category_name` VARCHAR(100) NOT NULL DEFAULT '',
      `shop_category_desc` VARCHAR(1000) DEFAULT '',
      `shop_category_img` VARCHAR(2000) DEFAULT NULL,
      `priority` INT(2) NOT NULL DEFAULT '0',
      `create_time` DATETIME DEFAULT NULL,
      `last_edit_time` DATETIME DEFAULT NULL,
      `parent_id` INT(11) DEFAULT NULL,
      PRIMARY KEY (`shop_category_id`),
      CONSTRAINT `fk_shop_category_self` FOREIGN KEY (`parent_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
    ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

为parent_id创建一个外键，将其与shop_category_id相关联。

**7.店铺**:ID，权重，图片，店名，描述，联系方式，地址，区域ID，类别ID，用户ID，状态：店铺是否通过审核，创建时间，建议，修改时间。

    CREATE TABLE `tb_shop` (
      `shop_id` int(10) NOT NULL AUTO_INCREMENT,
      `owner_id` int(10) NOT NULL COMMENT '店铺创建人',
      `area_id` int(5) DEFAULT NULL,
      `shop_category_id` int(11) DEFAULT NULL,
      `parent_category_id` int(11) DEFAULT NULL,
      `shop_name` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
      `shop_desc` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
      `shop_addr` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
      `phone` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
      `shop_img` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
      `longitude` double(16,12) DEFAULT NULL,
      `latitude` double(16,12) DEFAULT NULL,
      `priority` int(3) DEFAULT '0',
      `create_time` datetime DEFAULT NULL,
      `last_edit_time` datetime DEFAULT NULL,
      `enable_status` int(2) NOT NULL DEFAULT '0',
      `advice` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
      PRIMARY KEY (`shop_id`),
      KEY `fk_shop_profile` (`owner_id`),
      KEY `fk_shop_area` (`area_id`),
      KEY `fk_shop_shopcate` (`shop_category_id`),
      KEY `fk_shop_parentcate` (`parent_category_id`),
      CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),
      CONSTRAINT `fk_shop_parentcate` FOREIGN KEY (`parent_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`),
      CONSTRAINT `fk_shop_profile` FOREIGN KEY (`owner_id`) REFERENCES `tb_person_info` (`user_id`),
      CONSTRAINT `fk_shop_shopcate` FOREIGN KEY (`shop_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

创建三个外键，店铺区域与tb_area表中的area_id相关联，店铺创建人owner与用户信息中的user相关联，店铺分类id与店铺分类表中的店铺id相关联。

**8.商品分类列表**：商品ID，店铺ID，产品分类名称，优先级，创建时间。

    CREATE TABLE `tb_product_category` (
      `product_category_id` int(11) NOT NULL AUTO_INCREMENT,
      `product_category_name` varchar(100) NOT NULL,
      `product_category_desc` varchar(500) DEFAULT NULL,
      `priority` int(2) DEFAULT '0',
      `create_time` datetime DEFAULT NULL,
      `last_edit_time` datetime DEFAULT NULL,
      `shop_id` int(20) NOT NULL DEFAULT '0',
      PRIMARY KEY (`product_category_id`),
      KEY `fk_procate_shop` (`shop_id`),
      CONSTRAINT `fk_procate_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
    
**9.详情图片**：图片ID，图片路径，图片描述，优先级，创建时间，商品ID。

    CREATE TABLE `tb_product_img` (
      `product_img_id` INT(20) NOT NULL AUTO_INCREMENT,
      `img_addr` VARCHAR(2000) NOT NULL,
      `img_desc` VARCHAR(2000) DEFAULT NULL,
      `priority` INT(2) DEFAULT '0',
      `create_time` DATETIME DEFAULT NULL,
      `product_id` INT(20) DEFAULT NULL,
      PRIMARY KEY (`product_img_id`),
      KEY `fk_proimg_product` (`product_id`),
      CONSTRAINT `fk_proimg_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

为商品id创建外键与商品关联。
    
**10.商品**：  ID，商品名，描述，缩略图，原价，折扣价，权重，创建时间，修改时间，状态，店铺ID，类别ID。

    CREATE TABLE `tb_product` (
      `product_id` INT(100) NOT NULL AUTO_INCREMENT,
      `product_name` VARCHAR(100) NOT NULL,
      `product_desc` VARCHAR(2000) DEFAULT NULL,
      `img_addr` VARCHAR(2000) DEFAULT '',
      `normal_price` VARCHAR(100) DEFAULT NULL,
      `promotion_price` VARCHAR(100) DEFAULT NULL,
      `priority` INT(2) NOT NULL DEFAULT '0',
      `create_time` DATETIME DEFAULT NULL,
      `last_edit_time` DATETIME DEFAULT NULL,
      `enable_status` INT(2) NOT NULL DEFAULT '0',
      `product_category_id` INT(11) DEFAULT NULL,
      `shop_id` INT(20) NOT NULL DEFAULT '0',
      PRIMARY KEY (`product_id`),
      CONSTRAINT `fk_product_procate` FOREIGN KEY (`product_category_id`) REFERENCES `tb_product_category` (`product_category_id`),
      CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
    ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

为商品id创建外键与商品分类相关联，为店铺id创建外键与店铺相关联。

###数据库中各表之间的关联


1.用户信息

<div align="center"> <img src="../img/personinfo.png" width="450"/> </div><br>

用户信息表通过用户id(user_id)与本地账号和微信账号相关联。

2.店铺信息

<div align="center"> <img src="../img/shopinfo.png" width="450"/> </div><br>

店铺信息与区域信息通过区域id相关联，与店铺类别通过店铺类别id相关联，商品信息与商品类别类似。

3.商品信息

<div align="center"> <img src="../img/productinfo.png" width="450"/> </div><br>

# 三、项目构建 #
在src/main/resources目录下创建spring和mapper两个文件夹。

resouces文件夹一般存放静态资源。

web-INF文件夹中的文件浏览器无法直接访问，web.xml文件一般用来初始化配置。


1.创建web包，主要存放controller层代码。
2.创建service包，存放业务逻辑层代码，并创建实现类的包。
3.创建dao包，存放对数据库操作的相关代码，包括redis缓存，直接在配置文件mapper中实现到的逻辑。
4.创建dto包，弥补entity包的不足，比如封装对象的一些状态信息。
5.创建enums包，，存放枚举类。
6.创建interceptor包，存放拦截器相关代码。
7.创建util包，存放公用的工具类。


## 通过pom.xml引入jar包 ##

1.junit

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>

 2.logback

        <!-- 1.日志 -->
		<!-- 实现slf4j接口并整合 -->
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.2.3</version>
		</dependency>
3.spring相关

<!-- 4.Spring -->
**<!-- 1)Spring核心 -->**

Spring核心 工具类。spring其他组件都要使用这个包里的类，是其他组件的基本核心

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>${spring.version}</version>
		</dependency>

 这个jar文件是所有应用都要用到的，它包含访问配置文件、创建和管理bean以及进行控制反转和依赖注入操作相关的类。如果应用只需要基本的IOC和DI，只需要引入spring-core和spring-beans 

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-beans</artifactId>
			<version>${spring.version}</version>
		</dependency>

 这个jar文件为Spring核心提供大量的扩展。可以找到使用Spring ApplicationContext特性所需的全部类，instrumentation组件以及校验Validation相关的类 
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>${spring.version}</version>
		</dependency>

** 2)Spring DAO层 **

 这个jar文件包含对Spring对JDBC数据访问进行封装的所有类 

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>${spring.version}</version>
		</dependency>

为JDBC，Mybatis提供一致的声明式和编程式事物管理 

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-tx</artifactId>
			<version>${spring.version}</version>
		</dependency>

** 3)Spring web **

Spring web包含Web应用开发时，用Spring框架时所需的核心类，包括自动载入WebApplicaitonCore 

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${spring.version}</version>
		</dependency>

包含SpringMVC框架相关的类

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>${spring.version}</version>
		</dependency>

<!-- 4)Spring test -->

Spring test对JUNIT等测试框架进行简单的封装

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-test</artifactId>
			<version>${spring.version}</version>
		</dependency>


4.servlet web相关

<!-- 3.Servlet web -->
	<dependency>
		<groupId>taglibs</groupId>
		<artifactId>standard</artifactId>
		<version>1.1.2</version>
	</dependency>
	<dependency>
		<groupId>jstl</groupId>
		<artifactId>jstl</artifactId>
		<version>1.2</version>
	</dependency>
	<!-- 解析json -->
	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.8.7</version>
	</dependency>

 提供servlet服务

	<dependency>
		<groupId>javax.servlet</groupId>
		<artifactId>javax.servlet-api</artifactId>
		<version>3.1.0</version>
	</dependency>

Map工具类 对标准java Collection的扩展,spring-core.jar需要commons-collections。jar提供基础 

	<dependency>
	    <groupId>commons-collections</groupId>
	    <artifactId>commons-collections</artifactId>
	    <version>3.2</version>
	</dependency>

5.mybatis相关

<!-- DAO: MyBatis -->
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis</artifactId>
		<version>3.4.2</version>
	</dependency>

spring-mybatis整合包

	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis-spring</artifactId>
		<version>1.3.1</version>
	</dependency>

6.数据库相关

<!-- 2.数据库 -->

-- 支持jdbc与数据库交互 -->
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>5.1.37</version>
		<scope>runtime</scope>
	</dependency>
连接池 
	<dependency>
		<groupId>c3p0</groupId>
		<artifactId>c3p0</artifactId>
		<version>0.9.1.2</version>


## 建立数据库连接的配置文件 ##

1.jdbc.properties

    jdbc.driver=com.mysql.jdbc.Driver//驱动器
    jdbc.url=jdbc:mysql://localhost:3306/o2odb?//useUnicode=true&characterEncoding=utf8
    jdbc.username=root
    jdbc.password=
2.mybatis-config.xml

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
      PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
      "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
	<!-- 配置全局属性 -->
	<settings>
使用jdbc的getGeneratedKeys获取数据库自增主键值
		<setting name="useGeneratedKeys" value="true" />

使用列别名替换列名 默认:true 
		<setting name="useColumnLabel" value="true" />

开启驼峰命名转换:Table{create_time} -> Entity{createTime} 
		<setting name="mapUnderscoreToCamelCase" value="true" />
打印查询语句 
		<setting name="logImpl" value="STDOUT_LOGGING" />
	</settings>
`</configuration> ` 

3.spring相关配置

至下而上进行配置

spring-dao.xml

spring命名空间相关配置，以及各种标签的配置，一般都是差不多的，可以直接粘贴复制
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">


**配置整合mybatis过程 **

1.配置数据库相关参数properties的属性：${url}
	
    <context:property-placeholder location="classpath:jdbc.properties" />

2.数据库连接池
	


spring-service.xml

spring-web.xml


4.web.xml


## SSM重点 ##
SpringMVC：DispatcherServlet

Spring：IOC和AOP。AOP中的动态代理。


## 日志配置 ##