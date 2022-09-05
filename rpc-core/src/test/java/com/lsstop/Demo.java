package com.lsstop;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lsstop.transport.netty.server.NettyServer;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.catalog.ImmutableServiceWeights;
import org.junit.Test;

import java.util.UUID;

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
        Consul consul = Consul.builder().build();
        AgentClient agentClient = consul.agentClient();


        Registration.RegCheck deadManSwitch = ImmutableRegCheck.builder()
                .tcp("localhost:8080")
                .id(UUID.randomUUID().toString())
                .interval("5s")
                .deregisterCriticalServiceAfter("5s")
                .timeout("5s")
                .build();

        String serviceId = "1";
        Registration service = ImmutableRegistration.builder()
                .id(serviceId)
                .name("tcp")
                .port(8080)
                .address("localhost")
                .serviceWeights(ImmutableServiceWeights.builder().passing(3).warning(5).build())
                .check(deadManSwitch)
                .build();


        agentClient.register(service);
        Thread.sleep(3000000);
    }

    @Test
    public void demo4() throws Exception {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        System.out.println(stack[stack.length - 1].getClassName());
    }

    @Test
    public void demo5() throws Exception {
    }

}
