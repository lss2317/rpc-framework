package com.lsstop.proxy;

import com.lsstop.annotation.RpcClient;
import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.transport.netty.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端动态代理
 *
 * @author lss
 * @date 2022/08/18
 */
public class RpcClientProxy implements InvocationHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(RpcClientProxy.class);

    /**
     * 请求缓存
     */
    public static final ConcurrentHashMap<String, CompletableFuture<RpcResponse>> REQUEST_CACHE = new ConcurrentHashMap<>();

    private final NettyClient client;

    public RpcClientProxy(NettyClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOGGER.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        //通过反射获取服务名称
        Class<?> aClass = method.getDeclaringClass();
        if (!aClass.isAnnotationPresent(RpcClient.class)) {
            throw new RpcException(RpcErrorEnum.NOT_SETUP_SERVICE);
        }
        RpcClient annotation = aClass.getAnnotation(RpcClient.class);
        String serviceName = annotation.value();
        if ("".equals(serviceName)) {
            throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
        }
        String interfaceName = method.getDeclaringClass().getName();
        interfaceName = interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
        RpcRequest request = RpcRequest.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .name(serviceName)
                .interfaceName(interfaceName)
                .method(method.getName())
                .args(args)
                .argsType(method.getParameterTypes()).build();
        RpcResponse response = null;
        try {
            //远程调用请求
            CompletableFuture<RpcResponse> future = client.remoteService(request);
            response = future.get();
        } catch (Exception e) {
            LOGGER.error("方法调用请求发送失败:{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.TRANSFER_SERVICE_FAIL);
        }
        return response.getData();
    }
}