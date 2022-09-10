# Rpc-Framework

RPC-Framework是一款多注册中心RPC框架 . 基于Netty实现 , 并且实现了多种序列化与负载均衡算法.

## 架构

![image-20220907165158615](https://typora-1307541812.cos.ap-nanjing.myqcloud.com/image-20220907165158615.png)

消费端通过接口，调用动态代理生成的对象，通过Netty异步调用，服务端则调用消费端调用的服务，返回执行结果。

## 特性

- 

## 模块说明

- rpc-common 通用实体对象，工具类
- rpc-core rpc核心实现

## 传输协议

调用参数与返回值的传输采用了如下协议以防止粘包：

```
+---------------+-----------------+---------------+
|  Magic Number | Serializer Type | Data Length   |
|    4 bytes    |      4 bytes    |   4 bytes     |
+---------------+-----------------+---------------+
|                   Data Bytes                    |
|              Length: ${Data Length}             |
+-------------------------------------------------+
```

| 字段            | 解释                                                         |
| :-------------- | :----------------------------------------------------------- |
| Magic Number    | 魔数，识别是否是自己的协议包，0xCAFEBABE                     |
| Serializer Type | 序列化器类型，标明这个包的数据的序列化方式                   |
| Data Length     | 数据字节的长度                                               |
| Data Bytes      | 传输的对象，通常是一个`RpcRequest`或`RpcResponse`对象，消费的传输的是`RpcRequest`,服务提供方传输的是`RpcResponse`,序列化方式取决于Serializer Type |

## 基本使用

### 服务提供方

**1、服务端定义接口**

```java
public interface Hello {

    String hello(String name);
}
```

**2、实现接口，并且标注`@RpcService`注解**

```java
@RpcService
public class HelloImpl implements Hello {

    @Override
    public String hello(String name) {
        return "你是：" + name;
    }
}
```

**3、服务端启动服务**

```java
@RpcServiceScan(value = "com.lsstop.service") //定义包扫描，不指定包则默认启动类所在包
public class Start {
    public static void main(String[] args) throws NacosException {
        //将服务注册到nacos注册中心，也可以使用consul、redis
        NacosUtil.addURL("DEMO", new URL("DEMO", "127.0.0.1", 9000, 5));
        //启动服务
        new NettyServer().start("127.0.0.1", 9000);
    }
}
```

### 消费端

**1、定义和消费方一样的接口，并标注`@RpcClient`注解，注解值为服务提供注册中心注册名称**

```java
@RpcClient(value = "DEMO")
public interface Hello {
    
    String hello(String name);
}
```

**2、调用服务**

```java
public class Test {
    public static void main(String[] args) {
        //负载均衡算法
        LoadBalance balance = new WeightRoundRobinBalance();
        //序列化方式
        CommonSerializer jsonSerializer = new FastJsonSerializer();
        //注册中心类型
        RegistryCenter registryCenter = new NacosRegistry(balance);
        //创建netty客户端
        NettyClient nettyClient = new NettyClient(registryCenter, jsonSerializer);
        //创建动态代理类
        RpcClientProxy clientProxy = new RpcClientProxy(nettyClient);
        //活动代理对象
        Hello clientProxyProxy = clientProxy.getProxy(Hello.class);
        //调用服务
        System.out.println(clientProxyProxy.hello("你好"));
    }
}
```

## spring支持

**1、将NettyClient注册为bean**

```java
@Configuration
public class BeanConfig {

    @Bean
    public NettyClient nettyClient(){
        return new NettyClient(new NacosRegistry(new WeightRoundRobinBalance()),new FastJsonSerializer());
    }

}
```

**2、定义和消费方一样的接口，并标注`@RpcClient`注解，注解值为服务提供注册中心注册名称**

```java
@RpcClient(value = "DEMO")
public interface Hello {

    String hello(String name);
}
```

**3、编写service，里面通过`@RpcResource`注入，即可调用服务**

```java
@Service
public class TestService {

    @RpcResource
    Hello hello;

    public Hello getHello() {
        return hello;
    }
}
```

**4、调用服务**

```java
@Import(SpringRpcConfig.class)
public class MainApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext();
        //包扫描
        applicationContext.scan("com.lsstop");
        //刷新
        applicationContext.refresh();
        TestService bean = applicationContext.getBean(TestService.class);
        System.out.println(bean.getHello().hello("你好"));
    }
}
```

`@Import`注解引入`SpringRpcConfig`开启服务
