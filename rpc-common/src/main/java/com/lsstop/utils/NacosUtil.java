package com.lsstop.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lss
 * @date 2022/08/28
 */
public class NacosUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(NacosUtil.class);

    /**
     * nacos启动地址
     */
    private static String serverAddress = "localhost:8848";

    private static NamingService service;

    public static void setNacos(String serverAddress) {
        NacosUtil.serverAddress = serverAddress;
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param url         服务信息
     */
    public static void addURL(String serviceName, URL url) throws NacosException {
        init();
        Instance instance = new Instance();
        instance.setIp(url.getHost());
        instance.setPort(url.getPort());
        instance.setWeight(url.getWeight());
        service.registerInstance(serviceName, instance);
    }


    /**
     * 服务下线
     */
    public static void deleteURL(String serviceName, URL url) throws NacosException {
        service.deregisterInstance(serviceName, url.getHost(), url.getPort());
    }

    /**
     * 获取url集合
     *
     * @param serviceName 服务名称
     * @return list
     */
    public static List<URL> getURLList(String serviceName) throws NacosException {
        init();
        List<Instance> instances = service.selectInstances(serviceName, true);
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        return instances.stream().map(instance -> new URL(instance.getServiceName(), instance.getIp(), instance.getPort(), (int) instance.getWeight())).collect(Collectors.toList());

    }

    private static void init() {
        if (service == null) {
            synchronized (NacosUtil.class) {
                if (service == null) {
                    service = getService();
                }
            }
        }
    }

    private static NamingService getService() {
        try {
            return NamingFactory.createNamingService(serverAddress);
        } catch (NacosException e) {
            LOGGER.error("连接注册中心失败：{}", serverAddress);
            throw new RpcException(RpcErrorEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
}
