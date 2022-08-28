package com.lsstop.transport.netty.client;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.proxy.RpcClientProxy;
import com.lsstop.registry.Redis.RedisRegistry;
import com.lsstop.registry.RegistryCenter;
import com.lsstop.serializable.CommonSerializer;
import com.lsstop.transport.netty.codec.ClientDecoder;
import com.lsstop.transport.netty.codec.ClientEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NettyClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    /**
     * 储存与服务端的通道
     */
    private static final ConcurrentHashMap<String, Channel> channelCollect = new ConcurrentHashMap<>();

    /**
     * 注册中心
     */
    private final RegistryCenter registryCenter;

    /**
     * 序列化
     */
    private final CommonSerializer serializer;

    public NettyClient(RegistryCenter registryCenter, CommonSerializer serializer) {
        this.registryCenter = registryCenter;
        this.serializer = serializer;
    }

    public static Bootstrap initBootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
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
        if (channelCollect.containsKey(url.toString())) {
            Channel channel = channelCollect.get(url.toString());
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelCollect.remove(url.toString());
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
                pipeline.addLast("encoder", commonEncoder);
                pipeline.addLast("decoder", new ClientDecoder());
                //自定义处理器
                pipeline.addLast("handler", new NettyClientHandler());
            }
        });
        Channel channel = null;
        try {
            channel = connect(bootstrap, new InetSocketAddress(url.getHost(), url.getPort()));
        } catch (Exception e) {
            return null;
        }
        channelCollect.put(url.toString(), channel);
        return channel;
    }

    /**
     * 远程服务调用
     *
     * @param request 请求信息
     * @return 响应线程
     */
    public CompletableFuture<RpcResponse> remoteService(RpcRequest request) {
        if (serializer == null) {
            LOGGER.info("未设置序列化器");
            throw new RpcException(RpcErrorEnum.SERIALIZER_NOT_FOUND);
        }
        //向注册中心获取服务提供方URL
        URL url = registryCenter.getURL(request.getName());
        if (url == null) {
            throw new NullPointerException("无此服务");
        }
        Channel channel = getChannel(url, serializer);
        if (channel == null || !channel.isActive()) {
            //连接异常，服务下线（redis注册中心）
            if (registryCenter instanceof RedisRegistry) {
                registryCenter.delURL(request.getName(), url);
            }
            throw new RpcException(RpcErrorEnum.CLIENT_CONNECT_SERVER_FAILURE);
        }
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        //缓存请求
        RpcClientProxy.REQUEST_CACHE.put(request.getId(), future);
        try {
            channel.writeAndFlush(request).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    LOGGER.info("客户端调用服务：{},参数：{}", request.getName(), request);
                } else {
                    future.completeExceptionally(future1.cause());
                    future1.channel().close();
                }
            });
        } catch (Exception e) {
            //清除缓存
            RpcClientProxy.REQUEST_CACHE.remove(request.getId());
            Thread.currentThread().interrupt();
        }
        return future;
    }

    /*
     *因为netty是异步，所以用CompletableFuture，不然会not isActive
     */
    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                LOGGER.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                completableFuture.completeExceptionally(new RpcException(""));
            }
        });
        return completableFuture.get();
    }

}
