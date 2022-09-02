package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

import java.util.List;
import java.util.Random;

/**
 * 加权随机
 *
 * @author lss
 * @date 2022/09/02
 */
public class WeightRandomBalance implements LoadBalance {


    @Override
    public URL select(List<URL> list) {
        if (list == null || list.isEmpty()) {
            throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
        }
        int sum = list.stream().mapToInt(URL::getWeight).sum();
        int randomInt = new Random().nextInt(sum);
        for (URL url : list) {
            if (url.getWeight() > randomInt) {
                return url;
            }
            randomInt -= url.getWeight();
        }
        //没有查询到服务，直接抛出异常
        throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
    }
}
