package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

import java.util.List;
import java.util.Random;

/**
 * 随机
 *
 * @author lss
 * @date 2022/08/25
 */
public class RandomBalance implements LoadBalance {

    static Random random = new Random();

    @Override
    public URL select(List<URL> list) {
        return list.get(random.nextInt(list.size()));
    }
}
