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

ApplicationContext 是 BeanFactory 的高级表现形式。

Resource 是 Spring 用来封装 I/O 操作的类。

##### 3. ApplicationContext 应用场景


ApplicationContext 提供了 BeanFactory 不具备的新特性。

- 支持不同的信息源。 ApplicationContext 扩展了 MessageSource 接口，这些信息源的扩展功能可以支持国际化的实现，为开发多语言版本的应用提供服务。
- 访问资源。 ResourceLoader(ResourcePatternResolver) 和 Resource 支持上，这样我们可以从不同的地方得到 Bean 定义资源。 这种抽象使用用户
程序可以灵活地定义Bean定义信息，尤其是从不同的 I/O 途径得到 Bean 定义信息。这在接口关系上看不出来，不过一般来说，具体 ApplicationContext 都是
继承了 DefaultResourceLoader 的子类。因为 DefaultResourceLoader 是 AbstractApplicationContext 的基类。
- 支持应用事件。继承了 `ApplicationEventPublisher` 从而在上下文中引入了事件机制。这些事件和Bean声明周期结合为 Bean 的管理提供了便利。
- 在 ApplicationContext 中提供附加服务。这些服务使得基本 IOC 容器的功能更丰富。因为具备了这些丰富的附加功能，使得 ApplicationContext 与
简单的 BeanFactory 相比，对它的使用是一种面向框架的使用风格，所以一般建议在开发应用时使用 ApplicationContext 作为 ICO 容器的基本形式。

##### 4. ApplicationContext 容器的设计原理

  在 ApplicationContext 容器中，我们以常用的 `FileSystemXmlApplicationContext` 的实现为列来说明 `ApplicationContext` 容器的设计原理。

  在 FileSystemXmlApplicationContext 的设计中，我们看到 ApplicationContext 应用上下文的主要功能已经在 FileSystemXmlApplication 的
基类 `AbstractXmlApplicationContext`中实现了，在`FileSystemXmlApplication`中，作为一个具体的应用上下文，只需要实现和它自身设计相关的两个
功能。
  一个功能是，如果应用直接使用 FileSystemXmlApplication ，对于实例化这个应用上下文的支持，同时启动 IOC 容器的 `refresh()` 过程。

### 2.3 IOC 容器的初始化过程

  IOC 容器初始化是由前面介绍的 `AbstractXmlApplicationContext` 的 `refresh()` 方法来启动的，这个方法标志这 IOC 容器的正式启动。具体来说，
这个启动包括 `BeanDefinition` 的 `Resource` 定位、载入和注册三个基本过程。如果我们了解如何编程式地使用 IOC 容器，就可以清楚地看到 Resource
定位和载入过程的接口调用。
  Spring 把这 是三个过程分开，并使用不同的模块来完成，如使用相应的 ResourceLoader、BeanDefinitionReader等模块，通过这样的设计方式，可以让
用户更加灵活地对这三个过程进行裁剪或扩展，定义出适合自己的 IOC 容器的初始化过程。
  1.  `Resource` 定位过程
     指的是 BeanDefinition 的资源定位，它由 `ResourceLoader` 通过统一的 Resource 接口来完成，这个 Resource 对各种形式的 `BeanDefinition`
     的使用都提供了统一接口。对于这些`BeanDefinition`的存在形式，相信大家都不会感到陌生。比如，在文件系统中的 Bean 定义信息可以使用 FileSystemResource
     来 进行抽象; 在类路径中的 Bean 定义信息可以使用前面提到的 ClassPathResource 来使用，等等。这个定位过程类似于容器新增数据的过程，就像水桶
     先把水找到一样。
  2. `BeanDefintion` 的载入。这个载入过程就是把用户定义好的 Bean 显示成 IOC 容器内部的数据结构，而这个容器内部的数据结构就是 `BeanDefinition`
     具体来说，这个 BeanDefinition 实际上就是 POJO 对象 在 IOC 容器中的抽象， 通过 这个 BeanDefinition定义的数据结构，使 IOC 容器能够
     方便地对 POJO 对象 也就是 Bean 进行管理。
  3. 向 IOC 容器注册这些 `BeanDefinition` 的过程。这个过程是通过调用 `BeanDefinitionRegistry`接口的实现来完成的。这个注册过程把载入过程中
     解析的 BeanDefinition 向 ICO 容器进行注册。 IOC 容器内部将 Bean Definition 注入到一个 HashMap 中去， IOC容器就是通过这个 HashMap
     来持有这些 Bean Definition 数据的。
  值得注意的是，这里谈的 IOC容器初始化过程，一般不包括 Bean 依赖注入的实现。在SpringIOC 的设计中，Bean 定义的载入和依赖注入是两个独立的过程。
  依赖注入一般发生在应用第一次通过 `getBean`向容器索取Bean的时候。

#### 2.3.1 Bean Definition 的 Resource 定位

   AbstractApplicationContext extends DefaultResourceLoader 

#### 2.3.2 BeanDefinition的载入和解析

  对于 Ioc 容器来说，这个载入过程，相当于把定义的 BeanDefinition 在 IOC 容器中转化为一个 Spring 内部表示的数据结构的过程。
  IoC 容器对 Bean 的管理和依赖注入功能的实现，是通过对其持有的 BeanDefinition 进行各种相关操作来完成的。这些 BeanDefinition 数据在 Ioc 容
  器中通过一个 HashMap 来保持和维护。
  



## 常见类

`BeanFactory`

`ApplicationContext`

`BeanPostProcessor` ->  

  初始化   

  在 初始化 Bean 前后做处理 `postProcessBeforeInitialization` 初始化前执行 `postProcessAfterInitialization` 初始化后执行

  ( `AbstractAutoProxyCreator`  )AOP 实现原理( cglib ， jdk 动态代理)
  
`BeanFactoryPostProcessor`  -> 用来处理 Bean 的 定义信息的

`Environment` -> 为了方便使用，在容器创建的时候会提前加系统相关属性加载到 `StandardEnvironment` 对象中，方便后续使用

`BeanDefinition` -> IOC 容器存储内部的数据结构

`BeanDefinitionHolder` -> 持有一个 BeanDefinition 、beanDefinition 的 beanName 和 alias

