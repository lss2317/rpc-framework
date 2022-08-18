package com.lsstop.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private String host;

    private int port;


    public NettyClient() {
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    //是否开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            LOGGER.info("服务连接成功");
            //保存channel
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.info("注册服务失败");
        } finally {
            group.shutdownGracefully();
        }
    }

    public Object sendMessage() {
        return null;
    }

}
