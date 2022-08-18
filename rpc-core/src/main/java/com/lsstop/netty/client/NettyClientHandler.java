package com.lsstop.netty.client;

import com.lsstop.entity.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcRequest> {

    /**
     * 收集储存的服务
     */
    private Map<String, Class<?>> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //扫描服务，发送连接请求
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {

        if (msg.getIsResponse() != null && msg.getIsResponse()) {
            //返回结果

            return;
        }
        //获取请求，调用方法，返回结果(使用代理)


    }
}
