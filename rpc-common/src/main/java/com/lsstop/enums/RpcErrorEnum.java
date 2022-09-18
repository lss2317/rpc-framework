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

    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SCAN_PACKAGE_NOT_FOUND("未添加包扫描"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    SERIALIZER_NOT_FOUND("未设置序列化器"),
    REGISTERED_NOT_FOUND("未设置注册中心"),
    SERVICE_TRANSFER_ERROR("服务连接错误"),
    TRANSFER_SERVICE_FAIL("调用服务失败"),
    SERVICE_RESPONSE_FAILURE("服务响应失败"),
    NOT_FOUND_SERVICE("未发现服务"),
    NOT_SETUP_SERVICE("未设置服务名称"),
    SCANNING_SERVICE_ERROR("扫描服务错误"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接注册中心失败"),
    REGISTER_SERVICE_FAILED("注册服务失败");

    private final String message;
}
