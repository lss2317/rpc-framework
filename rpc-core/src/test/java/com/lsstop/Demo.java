package com.lsstop;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.lsstop.entity.URL;
import com.lsstop.loadbalancer.PollingBalance;
import com.lsstop.transport.netty.server.NettyServer;
import com.lsstop.utils.ConsulUtil;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.catalog.ImmutableServiceWeights;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
//        ConsulUtil.addURL("DEMO",new URL("DEMO","127.0.0.1",8081,6));
        double b = 1.12D;
        System.out.println(b);
    }

    @Test
    public void demo5() throws Exception {
      List<URL> list = new ArrayList<>();
      list.add(new URL("1","1",1,1));
      list.add(new URL("2","2",2,2));
      list.add(new URL("3","3",3,3));
      list.add(new URL("4","4",4,4));
        PollingBalance balance = new PollingBalance();
        for (int i = 0; i < 20; i++) {
            URL select = balance.select(list);
            System.out.println(select);
        }
    }

}
