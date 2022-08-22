package com.lsstop.provider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务注册名称提供
 *
 * @author lss
 * @date 2022/08/20
 */
public class RegisterNameProvider {

    /**
     * 储存服务提供方名称
     */
    private static final ConcurrentHashMap<String, String> serviceNameCollect = new ConcurrentHashMap<>();


    /**
     * 添加
     *
     * @param interfaceName 接口名称
     * @param serviceName   服务提供方注册名称
     */
    public static void addServiceName(String interfaceName, String serviceName) {
        serviceNameCollect.put(interfaceName, serviceName);
    }

    /**
     * 获取
     *
     * @param interfaceName 接口名称
     * @return 服务提供方注册名称
     */
    public static String getServiceName(String interfaceName) {
        return serviceNameCollect.get(interfaceName);
    }
}
