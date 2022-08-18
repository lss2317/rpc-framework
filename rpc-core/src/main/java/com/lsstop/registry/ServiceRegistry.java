package com.lsstop.registry;

import io.netty.channel.ChannelHandlerContext;

/**
 * 注册服务接口
 * @author lss
 * @date 2022/08/17
 */
public interface ServiceRegistry {

    void registryService(ChannelHandlerContext context,String serviceName);
}
