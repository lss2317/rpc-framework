package com.lsstop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lss
 * @date 2022/08/20
 */
@Getter
@AllArgsConstructor
public enum RequestType {

    /**
     * 请求
     */
    REQUEST(0),

    /**
     * 响应
     */
    RESPONSE(1);

    private final int type;
}
