package com.lsstop.netty.adapter;

/**
 * rpc服务接口
 *
 * @author lss
 * @date 2022/08/17
 */
public interface RpcServer {

    /**
     * 启动服务
     */
    void start();

    /**
     * 注册服务
     *
     * @param service     服务类型
     * @param serviceName 服务名称
     */
    <T> void registrationService(T service, String serviceName);
}
