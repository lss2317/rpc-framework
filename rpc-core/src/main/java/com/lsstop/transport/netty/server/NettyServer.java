package com.lsstop.transport.netty.server;


import com.lsstop.handler.InvokeMethodHandler;
import com.lsstop.transport.netty.codec.ServerDecoder;
import com.lsstop.transport.netty.codec.ServerEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lss
 * @date 2022/08/17
 */
public class NettyServer {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);


    public void start(String host, int port) {
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
                            pipeline.addLast("decoder", new ServerDecoder());
                            pipeline.addLast("encoder", new ServerEncoder());
                            //添加自定义handler
                            pipeline.addLast("handler", new NettyServerHandler(new InvokeMethodHandler()));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            LOGGER.info("rpc服务启动成功...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.info("rpc服务启动错误:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
