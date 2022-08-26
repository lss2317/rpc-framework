package com.lsstop.transport.netty.server;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.handler.InvokeMethodHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
        if (msg.getHeartBeat() != null && msg.getHeartBeat()) {
            LOGGER.info("接收到心跳包...");
            return;
        }
        //TODO 调用服务，并且返回结果
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


    /**
     * 心跳检测，检测连接是否断开
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOGGER.info("hello");
        //判断是否长时间没有发送心跳包，及时断开连接
        if (evt instanceof IdleStateEvent) {
            IdleState idleState = ((IdleStateEvent) evt).state();
            if (idleState == IdleState.READER_IDLE) {
                LOGGER.error("长时间未发送心跳包,断开连接...");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
