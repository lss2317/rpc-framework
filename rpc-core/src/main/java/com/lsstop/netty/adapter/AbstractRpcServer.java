package com.lsstop.netty.adapter;

/**
 * rpc服务注册抽象实体
 *
 * @author lss
 * @date 2022/08/17
 */
public abstract class AbstractRpcServer implements RpcServer {


    @Override
    public <T> void registrationService(T service, String serviceName) {

    }
}
