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

## 传输协议（SRF协议）

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
| Data Bytes      | 传输的对象，通常是一个`RpcRequest`或`RpcClient`对象，取决于`Package Type`字段，对象的序列化方式取决于`Serializer Type`字段。 |

## 使用
