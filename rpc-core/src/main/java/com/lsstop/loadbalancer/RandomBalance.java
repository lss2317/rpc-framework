package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

import java.util.List;
import java.util.Random;

/**
 * 随机
 *
 * @author lss
 * @date 2022/08/25
 */
public class RandomBalance implements LoadBalance {

    @Override
    public URL select(List<URL> list) {
        if (list == null || list.isEmpty()) {
            throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
