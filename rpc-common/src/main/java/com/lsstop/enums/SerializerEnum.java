package com.lsstop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lss
 * @date 2022/08/20
 */
@Getter
@AllArgsConstructor
public enum SerializerEnum {

    FASTJSON(1),
    JACKSON(2);

    private final int type;
}
