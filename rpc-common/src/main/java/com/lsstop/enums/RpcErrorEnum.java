package com.lsstop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lss
 * @date 2022/08/20
 */
@Getter
@AllArgsConstructor
public enum RpcErrorEnum {

    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    SERVICE_RESPONSE_FAILURE("服务响应失败");

    private final String message;
}
