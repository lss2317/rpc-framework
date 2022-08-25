package com.lsstop.registry.Redis;

import com.lsstop.entity.URL;
import com.lsstop.loadbalancer.LoadBalance;
import com.lsstop.registry.RegistryCenter;
import com.lsstop.utils.RedisUtil;

import java.util.List;

/**
 * @author lss
 * @date 2022/08/20
 */

public class RedisRegistry implements RegistryCenter {

    private final LoadBalance balance;

    public RedisRegistry(LoadBalance balance) {
        this.balance = balance;
    }

    @Override
    public void registered(String serviceName, URL url) {
        RedisUtil.addURL(serviceName, url);
    }

    @Override
    public URL getURL(String serviceName) {
        List<URL> urlList = RedisUtil.getURLList(serviceName);
        if (urlList == null || urlList.size() == 0) {
            return null;
        }
        //负载均衡算法进行选择
        return balance.select(urlList);
    }
}
