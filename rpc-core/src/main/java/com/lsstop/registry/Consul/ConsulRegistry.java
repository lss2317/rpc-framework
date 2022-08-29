package com.lsstop.registry.Consul;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.loadbalancer.LoadBalance;
import com.lsstop.registry.RegistryCenter;
import com.lsstop.utils.ConsulUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * consul注册中心
 *
 * @author lss
 * @date 2022/08/29
 */
public class ConsulRegistry implements RegistryCenter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConsulRegistry.class);

    private final LoadBalance balance;

    public ConsulRegistry(LoadBalance balance) {
        this.balance = balance;
    }

    @Override
    public void registered(String serviceName, URL url) {
        try {
            ConsulUtil.addURL(serviceName, url);
            LOGGER.info("服务 {}:{} 注册成功", url.getHost(), url.getPort());
        } catch (Exception e) {
            LOGGER.error("服务注册失败：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public URL getURL(String serviceName) {
        try {
            List<URL> list = ConsulUtil.getURLList(serviceName);
            return balance.select(list);
        } catch (Exception e) {
            LOGGER.error("服务调用报错：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.TRANSFER_SERVICE_FAIL);
        }
    }

    @Override
    public void delURL(String serviceName, URL url) {
        //consul有自动健康检查，自动删除，所以空实现
    }
}
