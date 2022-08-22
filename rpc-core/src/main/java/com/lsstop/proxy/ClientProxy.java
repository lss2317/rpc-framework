package com.lsstop.proxy;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.provider.RegisterNameProvider;
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
public class ClientProxy implements InvocationHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientProxy.class);

    /**
     * 请求缓存
     */
    public static final ConcurrentHashMap<String, CompletableFuture<RpcResponse>> REQUEST_CACHE = new ConcurrentHashMap<>();


    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //向注册中心调用rpc服务
        LOGGER.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        String serviceName = RegisterNameProvider.getServiceName(method.getDeclaringClass().getName());
        if (serviceName == null) {
            throw new NullPointerException();
        }
        RpcRequest request = RpcRequest.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .name(serviceName)
                .interfaceName(method.getDeclaringClass().getName())
                .method(method.getName())
                .args(args)
                .argsType(method.getParameterTypes()).build();
        RpcResponse response = null;
        try {
            //远程调用请求
        }catch (Exception e){

        }
        return request;
    }
}
