package com.lsstop.loadbalancer;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 负载均衡算法接口
 *
 * @author lss
 * @date 2022/08/18
 */
public interface LoadBalance {

    /**
     * 选择器
     *
     * @param serviceName 服务名称
     * @param list        通道集合
     * @return 全局通道
     */
    ChannelHandlerContext select(String serviceName, List<ChannelHandlerContext> list);
}
