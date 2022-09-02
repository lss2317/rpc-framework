package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

import java.util.List;

/**
 * 轮询
 *
 * @author lss
 * @date 2022/08/28
 */
public class RoundRobinBalance implements LoadBalance {

    private int NEXT_INDEX = 0;

    @Override
    public URL select(List<URL> list) {
        if (list == null || list.isEmpty()) {
            throw new RpcException(RpcErrorEnum.NOT_FOUND_SERVICE);
        }
        NEXT_INDEX = NEXT_INDEX == 0x7fffffff ? 0 : NEXT_INDEX + 1;
        int index = NEXT_INDEX % list.size();
        return list.get(index);
    }
}