package com.lsstop.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册服务提供
 *
 * @author lss
 * @date 2022/08/18
 */
public class ServiceProvider {


    /**
     * 储存暴露服务的类
     */
    private final static ConcurrentHashMap<String, Object> serviceCollect = new ConcurrentHashMap<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProvider.class);


    /**
     * 注册服务
     *
     * @param interfaceName 接口类型
     * @param o             实现类
     */
    public static void addServiceRegistration(String interfaceName, Object o) {
        serviceCollect.put(interfaceName, o);
    }

    /**
     * 获取服务
     *
     * @param interfaceName 服务名称
     * @return 服务类型
     */
    public static Object getService(String interfaceName) {
        return serviceCollect.get(interfaceName);
    }
}
