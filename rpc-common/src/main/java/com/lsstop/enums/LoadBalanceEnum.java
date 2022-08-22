package com.lsstop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lss
 * @date 2022/08/18
 */
@Getter
@AllArgsConstructor
public enum LoadBalanceEnum {

    /**
     * 轮陷
     */
    POLLING(0),

    /**
     * 随机
     */
    RANDOM(1);

    private final Integer balance;
}
