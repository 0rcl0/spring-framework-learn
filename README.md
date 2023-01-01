# spring-framework-learn
Spring 技术内幕学习


## 第二章   Spring Framework 的核心: IOC 容器实现

### 2.1 Spring IOC 容器概述

#### 2.1.1 IOC 容器和依赖反转模式

> 依赖反转，依赖对象的获取被反转了。
> 依赖注入

    接口注入、setter注入、构造器注入。

### 2.2 IOC 容器系列的设计与实现:BeanFactory 和 ApplicationContext

#### 2.2.1 Spring IOC 容器系列

BeanFactory 接口 是一个最基本的接口类


- 从接口 BeanFactory 到 HierarchicalBeanFactory，再到 ConfigurableBeanFactory，是一条主要的 BeanFactory设计路径。

 BeanFactory <- HierarchicalBeanFactory <- ConfigurableBeanFactory

BeanFactory 接口定义类基本的 IOC 容器规范。 包括 `getBean()`这样的 IOC 容器的基本方法 （通过这个方法可以从容器总获取Bean）。
HierarchicalBeanFactory 继承了 BeanFactory 的基本接口之后，增加了 `getParentBeanFacotry()`的接口功能，使BeanFactory 具有双亲 IOC 
容器的管理功能。
ConfigurableBeanFactory 主要定义一些对 BeanFactory 的配置功能，比如通过 `setParentBeanFacotry()` 设置双亲 IOC 容器，通过`addBeanPostProcessor()`
配置Bean后置处理器，等等。通过接口设计的叠加，定义了BeanFactory 就是简单 IOC 容器的基本功能。

- 第二条主线

BeanFactory <- ListableBeanFactory <- ApplicationContext <- WebApplicationContext/ConfigurableApplication
ListableBeanFactory和HierarchicalBeanFactory 两个接口，连接 BeanFactory 接口定义和 ApplicationContext 应用上下文的接口定义。
在 ListableBeanFactory 接口中，细化了许多 BeanFactory 的接口功能，比如定义了 `getBeanDefinitionNames()`接口方法；
对于 HierarchicalBeanFactory 接口
对于 ApplicationContext 接口，它通过 继承 MessageSource、ResourceLoader、ApplicationEventPublisher 接口，在BeanFactory 简单IOC
容器的基础上添加了许多对高级容器的特性的支持。

- 这里涉及的是主要接口关系，而具体的 IOC 容器都是在这个接口体系下实现的，比如 `DefaultListableBeanFactory`，这个基本 IOC 容器的实现就是实现
了 `ConfigurableBeanFactory`，从而成为一个简单 IOC 容器的实现。像其他 IOC 容器，比如 `XmlBeanFactory`，都是在 DefaultListableBeanFactory
的基础上做扩展，同样地， `ApplicationContext` 也是如此。
- 这个接口系统是以 `BeanFactory` 和 `ApplicationContext` 为核心的。 而 BeanFactory 又是 IOC 容器的最基本接口，在 `ApplicationContext`
的设计中，一方面，可以看到它继承了 `BeanFactory`接口体系中的 `ListableBeanFactory`、`AutowireCapableBeanFactory`、`HierarchicalBeanFactory`
等 BeanFactory 的接口，具备了 BeanFactory Ioc 容器的基本功能；另一方面，通过继承`MessageSource`、`ResourceLoader`、`ApplicationEventPublisher`
这些接口， BeanFactory 为 ApplicationContext 赋予了更高级的 IOC 容器特征。对于 ApplicationContext 而言，为了 在 Web 环境中使用它，
还设计了 `WebApplicationContext`接口，而这个接口通过继承 `ThemeSource` 接口来扩展功能。

##### 1.BeanFactory 应用场景

BeanFactory 和 FactoryBean

BeanFactory 的 `getBean()` 方法，使用 IOC 容器 API的主要方法，通过这个方法，可以取得 IOC 容器中管理的 Bean ，Bean的取得是通过名字来索引的。

##### 2. BeanFactory 容器设计原理

