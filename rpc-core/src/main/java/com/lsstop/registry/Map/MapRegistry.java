package com.lsstop.registry.Map;

import com.lsstop.registry.ServiceRegistry;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认注册方式，map
 *
 * @author lss
 * @date 2022/08/18
 */
public class MapRegistry implements ServiceRegistry {

    /**
     * 保存注册的通道
     */
    private static final Map<String, CopyOnWriteArrayList<ChannelHandlerContext>> channelMap = new ConcurrentHashMap<>();


    @Override
    public void registryService(ChannelHandlerContext context, String serviceName) {
        CopyOnWriteArrayList<ChannelHandlerContext> list = channelMap.get(serviceName);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        list.add(context);
        channelMap.put(serviceName, list);
    }
}
