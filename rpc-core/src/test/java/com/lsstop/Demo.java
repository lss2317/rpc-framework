package com.lsstop;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lsstop.entity.URL;
import com.lsstop.loadbalancer.HashBalance;
import com.lsstop.transport.netty.server.NettyServer;
import com.lsstop.utils.ConsulUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        NamingService service = NamingFactory.createNamingService("localhost:8848");
        Instance instance = new Instance();
        instance.setIp("localhost");
        instance.setPort(8080);
        instance.setWeight(3);
        service.registerInstance("DEMO1", instance);
        server.start("127.0.0.1", 8080);
    }

    @Test
    public void demo2() throws NacosException {
        NettyServer server = new NettyServer();
        NamingService service = NamingFactory.createNamingService("localhost:8848");
        Instance instance = new Instance();
        instance.setIp("localhost");
        instance.setPort(8081);
        instance.setWeight(5);
        service.registerInstance("DEMO", instance);
        server.start("127.0.0.1", 8081);
    }

    @Test
    public void demo3() throws Exception {
        List<URL> list = new ArrayList<>();
        list.add(new URL("DEMO", "127.0.0.1", 9000, 9));
        list.add(new URL("DEMO", "127.0.0.1", 9001, 9));
        list.add(new URL("DEMO", "127.0.0.1", 9002, 9));
        list.add(new URL("DEMO", "127.0.0.1", 9003, 9));
        list.add(new URL("DEMO", "127.0.0.1", 9004, 9));
        HashBalance balance = new HashBalance();
        System.out.println(new HashBalance().select(list));
        System.out.println(balance.select(list));
        System.out.println(balance.select(list));
        System.out.println(balance.select(list));
        System.out.println(balance.select(list));
    }

    @Test
    public void demo4() throws Exception {
    }

    @Test
    public void demo5() throws Exception {
        URL url1 = new URL("DEMO", "127.0.0.1", 9000, 9);
        URL url2 = new URL("DEMO", "127.0.0.1", 9000, 9,false);
        System.out.println(url1.equals(url2));
    }

}
