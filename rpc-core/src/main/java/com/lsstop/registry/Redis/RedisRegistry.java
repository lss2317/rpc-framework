package com.lsstop.registry.Redis;

import com.lsstop.entity.URL;
import com.lsstop.registry.RegistryCenter;

/**
 * @author lss
 * @date 2022/08/20
 */

public class RedisRegistry implements RegistryCenter {

    @Override
    public void registered(String serviceName, URL url) {

    }

    @Override
    public URL getURL(String serviceName) {
        return null;
    }
}
