package com.lsstop.provider;

/**
 * 服务提供方服务注册获取接口
 *
 * @author lss
 * @date 2022/08/18
 */
public interface ServiceProvider {

    /**
     * 注册服务
     *
     * @param service     服务类型
     * @param serviceName 服务名称
     */
    <T> void addServiceRegistration(T service, String serviceName);

    /**
     * 获取服务
     *
     * @param serviceName 服务名称
     * @return 服务类型
     */
    Object getService(String serviceName);
}
