package com.lsstop.transport.netty.client;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.entity.URL;
import com.lsstop.proxy.ClientProxy;
import com.lsstop.serializable.CommonSerializer;
import com.lsstop.transport.netty.codec.ClientDecoder;
import com.lsstop.transport.netty.codec.ClientEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    /**
     * 储存与服务端的通道
     */
    private static final ConcurrentHashMap<URL, Channel> channelCollect = new ConcurrentHashMap<>();

    /**
     * 序列化
     */
    private static Integer serializerType;


    public static Bootstrap initBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = null;
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

    /**
     * 获取连接通道
     *
     * @param url 服务端信息
     * @return 通道
     */
    public static Channel getChannel(URL url, CommonSerializer serializer) {
        if (channelCollect.containsKey(url)) {
            Channel channel = channelCollect.get(url);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelCollect.remove(url);
            }
        }
        //连接通道
        Bootstrap bootstrap = initBootstrap();
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //添加编码解码器
                ClientEncoder commonEncoder = new ClientEncoder();
                commonEncoder.setSerializer(serializer);
                pipeline.addLast(commonEncoder);
                pipeline.addLast(new ClientDecoder());
                //添加心跳处理器
                pipeline.addLast(new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS));
                //自定义处理器
                pipeline.addLast(new NettyClientHandler());
            }
        });
        Channel channel = null;
        try {
            ChannelFuture connect = bootstrap.connect(url.getHost(), url.getPort());
            channel = connect.channel();
        } catch (Exception e) {
            return null;
        }
        channelCollect.put(url, channel);
        return channel;
    }

    /**
     * 远程请求调用
     *
     * @param request 请求参数
     * @return 响应数据
     */
    public static CompletableFuture<RpcResponse> sendRequest(RpcRequest request) {

        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        //缓存请求
        ClientProxy.REQUEST_CACHE.put(request.getId(), future);
        //向注册中心获取服务提供方URL

        //获取通道，发送远程调用
        return null;
    }

}
