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

    JACKSON(1);

    private final int type;
}
