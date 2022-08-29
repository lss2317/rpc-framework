package com.lsstop.utils;

import com.google.common.net.HostAndPort;
import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.catalog.ImmutableServiceWeights;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author lss
 * @date 2022/08/29
 */
public class ConsulUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConsulUtil.class);

    /**
     * 默认ip
     */
    private static String DEFAULT_HOST = "localhost";

    /**
     * 默认端口
     */
    private static int DEFAULT_PORT = 8500;

    /**
     * 健康检查的频率
     */
    private static final String INTERVAL = "3s";

    /**
     * 连接超时时间
     */
    private static final String TIMEOUT = "1s";

    /**
     * 取消注册时间
     */
    private static final String DELETE_TIME = "3s";


    private static Consul consul;


    public static void setConsul(String defaultHost, int defaultPort) {
        DEFAULT_HOST = defaultHost;
        DEFAULT_PORT = defaultPort;
    }


    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param url         服务信息
     */
    public static void addURL(String serviceName, URL url) {
        init();
        Registration.RegCheck check = ImmutableRegCheck.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .tcp(url.getHost() + ":" + url.getPort())
                .interval(INTERVAL)
                .timeout(TIMEOUT)
                .deregisterCriticalServiceAfter(DELETE_TIME)
                .build();
        Registration registration = ImmutableRegistration.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .name(serviceName)
                .port(url.getPort())
                .address(url.getHost())
                .serviceWeights(ImmutableServiceWeights.builder().passing((int) url.getWeight()).warning((int) url.getWeight()).build())
                .check(check)
                .build();
        AgentClient client = consul.agentClient();
        client.register(registration);
    }

    /**
     * 获取url集合
     *
     * @param serviceName 服务名称
     * @return list
     */
    public static List<URL> getURLList(String serviceName) {
        HealthClient healthClient = consul.healthClient();
        List<ServiceHealth> response = healthClient.getHealthyServiceInstances(serviceName).getResponse();
        return response.stream().map(res -> {
            Service service = res.getService();
            return new URL(service.getService(), service.getAddress(), service.getPort(), service.getWeights().get().getPassing());
        }).collect(Collectors.toList());
    }

    private static Consul getConsul() {
        try {
            return Consul.builder().withHostAndPort(HostAndPort.fromParts(DEFAULT_HOST, DEFAULT_PORT)).build();
        } catch (Exception e) {
            LOGGER.error("连接注册中心失败：{}:{}", DEFAULT_HOST, DEFAULT_PORT);
            throw new RpcException(RpcErrorEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    private static void init() {
        if (consul == null) {
            synchronized (ConsulUtil.class) {
                if (consul == null) {
                    consul = getConsul();
                }
            }
        }
    }
}
