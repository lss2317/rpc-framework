package com.lsstop.transport.netty.client;

import com.lsstop.entity.RpcResponse;
import com.lsstop.proxy.RpcClientProxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        LOGGER.info("服务返回结果：{}", msg);
        //获取请求，调用方法，返回结果(使用代理)
        CompletableFuture<RpcResponse> future = RpcClientProxy.REQUEST_CACHE.get(msg.getId());
        if (future != null) {
            future.complete(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("远程调用异常：{}", cause.getMessage());
        ctx.close();
    }
}
