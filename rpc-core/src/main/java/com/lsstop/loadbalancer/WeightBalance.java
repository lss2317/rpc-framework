package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

import java.util.List;

/**
 * 权重算法
 *
 * @author lss
 * @date 2022/08/30
 */
public class WeightBalance implements LoadBalance{


    @Override
    public URL select(List<URL> list) {
        return null;
    }
}
