package com.lsstop.provider;

import com.lsstop.exception.NotFoundServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lss
 * @date 2022/08/18
 */
public class ServiceProviderImpl implements ServiceProvider {


    /**
     * 储存包扫描后注册的接口
     */
    private final Map<String, Object> serviceMap = new HashMap<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProviderImpl.class);


    @Override
    public <T> void addServiceRegistration(T service, String serviceName) {
        //TODO 后续再改，并发
        serviceMap.put(serviceName, service);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new NotFoundServiceException("未发现该服务");
        }
        return service;
    }
}
