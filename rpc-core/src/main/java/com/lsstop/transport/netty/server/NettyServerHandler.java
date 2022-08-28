package com.lsstop.transport.netty.server;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.handler.InvokeMethodHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lss
 * @date 2022/08/17
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    /**
     * 方法执行器
     */
    private final InvokeMethodHandler handler;

    public NettyServerHandler(InvokeMethodHandler invokeMethodHandler) {
        handler = invokeMethodHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        //调用服务，并且返回结果
        try {
            Object result = handler.invokeTargetMethod(msg);
            ctx.writeAndFlush(RpcResponse.success(result, msg.getId(), msg.getSerializerType()));
            //设置序列化号
        } catch (Exception e) {
            ctx.writeAndFlush(RpcResponse.fail(msg.getId(), msg.getSerializerType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("rpc远程调用出现异常...");
        ctx.close();
    }
}
