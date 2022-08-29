package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

import java.util.List;

/**
 * 轮询
 *
 * @author lss
 * @date 2022/08/28
 */
public class PollingBalance implements LoadBalance {


    @Override
    public URL select(List<URL> list) {
        return null;
    }
}
