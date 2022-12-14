package com.lsstop.registry.Redis;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.loadbalancer.LoadBalance;
import com.lsstop.registry.RegistryCenter;
import com.lsstop.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * redis注册中心
 *
 * @author lss
 * @date 2022/08/20
 */

public class RedisRegistry implements RegistryCenter {

    public static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistry.class);

    private final LoadBalance balance;

    public RedisRegistry(LoadBalance balance) {
        this.balance = balance;
    }

    @Override
    public void registered(String serviceName, URL url) {
        try {
            RedisUtil.addURL(serviceName, url);
            LOGGER.info("服务 {}:{} 注册成功", url.getHost(), url.getPort());
        } catch (Exception e) {
            LOGGER.error("服务注册失败：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public URL getURL(String serviceName) {
        try {
            List<URL> urlList = RedisUtil.getURLList(serviceName);
            if (urlList == null || urlList.size() == 0) {
                return null;
            }
            //负载均衡算法进行选择
            return balance.select(urlList);
        } catch (Exception e) {
            LOGGER.error("服务调用报错：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.TRANSFER_SERVICE_FAIL);
        }
    }

    @Override
    public void delURL(String serviceName, URL url) {
        try {
            RedisUtil.offlineURL(serviceName, url);
        } catch (Exception e) {
            LOGGER.error("服务下线失败：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.SERVICE_TRANSFER_ERROR);
        }
    }
}
