package com.lsstop.spring;

import com.lsstop.proxy.RpcClientProxy;
import com.lsstop.transport.netty.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * spring配置
 *
 * @author lss
 * @date 2022/09/07
 */
@Component
@Configuration
public class SpringRpcConfig {

    @Autowired(required = false)
    NettyClient client;

    @Bean("SpringBeanPostProcessor")
    public SpringBeanPostProcessor springBeanPostProcessor() {
        return new SpringBeanPostProcessor(rpcClientProxy());
    }

    public RpcClientProxy rpcClientProxy() {
        return new RpcClientProxy(client);
    }
}
