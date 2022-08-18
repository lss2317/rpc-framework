package com.lsstop.netty.server;


import com.lsstop.netty.codec.CommonDecoder;
import com.lsstop.netty.codec.CommonEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author lss
 * @date 2022/08/17
 */
public class NettyServer {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);


    public void start(String host, int port) {
        //JVM关闭前，清空注册服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //TODO 清空注册服务

        }));

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加心跳检测和编码解码器
                            pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new CommonEncoder());
                            pipeline.addLast(new CommonDecoder());
                            //添加自定义handler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            LOGGER.info("rpc服务启动成功...");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.info("rpc服务启动错误:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
