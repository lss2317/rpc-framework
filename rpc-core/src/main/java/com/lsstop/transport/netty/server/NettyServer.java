package com.lsstop.transport.netty.server;


import com.lsstop.annotation.RpcService;
import com.lsstop.annotation.RpcServiceScan;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.handler.InvokeMethodHandler;
import com.lsstop.provider.ServiceProvider;
import com.lsstop.transport.netty.codec.ServerDecoder;
import com.lsstop.transport.netty.codec.ServerEncoder;
import com.lsstop.utils.ReflectUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author lss
 * @date 2022/08/17
 */
public class NettyServer {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer() {
        scanServices();
    }

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
            LOGGER.info("rpc服务启动成功: {}:{}", host, port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.info("rpc服务启动错误:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 扫描服务
     */
    public void scanServices() {
        String mainClassName = ReflectUtil.getMainClassName();
        try {
            Class<?> aClass = Class.forName(mainClassName);
            if (!aClass.isAnnotationPresent(RpcServiceScan.class)) {
                LOGGER.error("没有配置包扫描@RpcServiceScan");
                throw new RpcException(RpcErrorEnum.SCAN_PACKAGE_NOT_FOUND);
            }
            String scanPackage = aClass.getAnnotation(RpcServiceScan.class).value();
            if ("".equals(scanPackage)) {
                //默认扫描主启动类下
                scanPackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
            }
            //通过包名扫描所有类
            Set<Class<?>> classSet = ReflectUtil.getClasses(scanPackage);
            //扫描服务
            for (Class<?> clazz : classSet) {
                //有@RpcService注解的类再获取服务
                if (clazz.isAnnotationPresent(RpcService.class)) {
                    //TODO 添加额外属性，解决一个接口有多个实现类的冲突问题
                    //RpcService rpcService = clazz.getAnnotation(RpcService.class);
                    Object obj = clazz.newInstance();
                    //TODO 接口添加注解报错问题
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        ServiceProvider.addServiceRegistration(anInterface.getName(), obj);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("服务扫描错误：{}", e.getMessage());
            throw new RpcException(RpcErrorEnum.SCANNING_SERVICE_ERROR);
        }
    }
}
