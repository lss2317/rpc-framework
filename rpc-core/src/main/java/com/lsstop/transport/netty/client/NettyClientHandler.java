package com.lsstop.transport.netty.client;

import com.lsstop.entity.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                LOGGER.info("{}发送心跳包", ctx.channel().remoteAddress());
                RpcRequest request = new RpcRequest();
                request.setHeartBeat(true);
                ctx.writeAndFlush(request);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        //获取请求，调用方法，返回结果(使用代理)


    }
}
