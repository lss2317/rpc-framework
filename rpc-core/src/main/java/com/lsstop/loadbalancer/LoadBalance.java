package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

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
}
