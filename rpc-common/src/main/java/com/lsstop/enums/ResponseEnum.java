package com.lsstop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码和信息
 *
 * @author lss
 * @date 2022/08/17
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(200, "请求响应成功"),
    FAIL(500, "调用方法失败"),
    METHOD_NOT_FOUND(500, "未找到指定方法"),
    CLASS_NOT_FOUND(500, "未找到指定类");

    private final int code;
    private final String message;
}
