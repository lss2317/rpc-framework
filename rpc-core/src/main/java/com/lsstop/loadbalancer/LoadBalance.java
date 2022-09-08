package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

import java.util.List;

/**
 * 负载均衡算法接口
 *
 * @author lss
 * @date 2022/08/18
 */
public interface LoadBalance {

    /**
     * 选择器
     *
     * @param list url集合
     * @return 服务url信息
     */
    URL select(List<URL> list);


    public static LoadBalance getBalance(String balance) {
        switch (balance) {
            case "random":
                return new RandomBalance();
            case "roundRobin":
                return new RoundRobinBalance();
            case "weightRandom":
                return new WeightRandomBalance();
            case "weightRoundRobin":
                return new WeightRoundRobinBalance();
            default:
                throw new RpcException(RpcErrorEnum.SERIALIZER_NOT_FOUND);
        }
    }
}
