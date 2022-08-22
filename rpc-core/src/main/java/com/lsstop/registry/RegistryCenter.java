package com.lsstop.registry;

import com.lsstop.entity.URL;

/**
 * @author lss
 * @date 2022/08/20
 */
public interface RegistryCenter {

    /**
     * 服务注册
     *
     * @param serviceName 服务名称
     * @param url         服务信息
     */
    void registered(String serviceName, URL url);
}
