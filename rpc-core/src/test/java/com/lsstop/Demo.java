package com.lsstop;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.lsstop.transport.netty.server.NettyServer;
import org.junit.Test;

/**
 * @author lss
 * @date 2022/08/27
 */
public class Demo {

    @Test
    public void demo() throws Exception {
        NamingService service = NamingFactory.createNamingService("localhost:8848");
        service.deregisterInstance("demo", "127.0.0.1", 9999);
        System.out.println("ok");
    }

    @Test
    public void demo1() throws NacosException {
        NettyServer server = new NettyServer();
//        NamingService service = NamingFactory.createNamingService("localhost:8848");
//        service.registerInstance("DEMO", "127.0.0.1", 8080);
        server.start("127.0.0.1", 8080);
    }

    @Test
    public void demo2() throws NacosException {
        NettyServer server = new NettyServer();
//        NamingService service = NamingFactory.createNamingService("localhost:8848");
//        service.registerInstance("DEMO", "127.0.0.1", 8081);
        server.start("127.0.0.1", 8081);
    }

}
