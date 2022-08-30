package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 *
 * @author lss
 * @date 2022/08/28
 */
public class PollingBalance implements LoadBalance {

    private final AtomicInteger NEXT_SERVER_COUNTER = new AtomicInteger(0);

    @Override
    public URL select(List<URL> list) {
        int current;
        int next;
        do {
            //首次初始化current为 1
            current = this.NEXT_SERVER_COUNTER.get();
            next = current >= 2147483647 ? 0 : current + 1;
            //compareAndSet方法判断两个参数值相等则返回true，否则返回false
        } while (!this.NEXT_SERVER_COUNTER.compareAndSet(current, next));
        return list.get(next % list.size());
    }
}