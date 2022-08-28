package com.lsstop.registry.Nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.loadbalancer.LoadBalance;
import com.lsstop.registry.RegistryCenter;
import com.lsstop.utils.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * nacos注册中心
 *
 * @author lss
 * @date 2022/08/28
 */
public class NacosRegistry implements RegistryCenter {

    public static final Logger LOGGER = LoggerFactory.getLogger(NacosRegistry.class);

    private final LoadBalance balance;

    public NacosRegistry(LoadBalance balance) {
        this.balance = balance;
    }

    @Override
    public void registered(String serviceName, URL url) {
        try {
            NacosUtil.addURL(serviceName, url);
            LOGGER.info("服务 {}:{} 注册成功", url.getHost(), url.getPort());
        } catch (NacosException e) {
            LOGGER.error("服务注册失败：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public URL getURL(String serviceName) {
        try {
            List<URL> list = NacosUtil.getURLList(serviceName);
            return balance.select(list);
        } catch (NacosException e) {
            LOGGER.error("服务调用报错：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.TRANSFER_SERVICE_FAIL);
        }
    }

    @Override
    public void delURL(String serviceName, URL url) {
        try {
            NacosUtil.deleteURL(serviceName, url);
            LOGGER.info("服务 {}:{} 下线", url.getHost(), url.getPort());
        } catch (NacosException e) {
            LOGGER.error("服务下线失败：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.SERVICE_TRANSFER_ERROR);
        }
    }
}
