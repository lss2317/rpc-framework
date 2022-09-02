package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

import java.util.List;

/**
 * 加权轮询
 *
 * @author lss
 * @date 2022/09/02
 */
public class WeightRoundRobinBalance implements LoadBalance {

    private int NEXT_INDEX = 0;

    @Override
    public URL select(List<URL> list) {
        if (list == null || list.isEmpty()) {
            throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
        }
        int sum = list.stream().mapToInt(URL::getWeight).sum();
        NEXT_INDEX = NEXT_INDEX == 0x7fffffff ? 0 : NEXT_INDEX + 1;
        int index = NEXT_INDEX % sum;
        for (URL url : list) {
            if (url.getWeight() > index) {
                return url;
            }
            index -= url.getWeight();
        }
        //没有查询到服务，直接抛出异常
        throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
    }
}
