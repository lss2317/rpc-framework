package com.lsstop.netty.server;

import com.lsstop.entity.RpcRequest;
import com.lsstop.registry.ServiceRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lss
 * @date 2022/08/17
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    private static ServiceRegistry registry;

    public NettyServerHandler() {
    }

    public NettyServerHandler(ServiceRegistry serviceRegistry) {
        registry = serviceRegistry;
    }

    /**
     * 保存注册的通道
     */
    private static final Map<String, CopyOnWriteArrayList<ChannelHandlerContext>> channelMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            if (msg.getHeartBeat() != null && msg.getHeartBeat()) {
                LOGGER.info("接收到心跳包...");
                return;
            }
            //服务注册
            if (msg.getConnect() != null && msg.getConnect()) {
                registry.registryService(ctx, msg.getName());
                LOGGER.info("{}注册成功", msg.getName());
                return;
            }
            //TODO 向服务提供方调用服务，并且返回结果
        } finally {
            //释放当前请求消息
            ReferenceCountUtil.release(msg);
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
