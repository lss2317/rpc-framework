package com.lsstop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc响应实体
 *
 * @author lss
 * @date 2022/08/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {

    /**
     * 请求id
     */
    private String id;

    /**
     * 响应状态码
     */
    private int statusCode;

    /**
     * 序列化类型
     */
    private int serializerType;

    /**
     * 响应数据
     */
    private Object data;

    public static RpcResponse success(Object data, String id, int serializerType) {
        RpcResponse response = new RpcResponse();
        response.setId(id);
        response.setSerializerType(serializerType);
        response.setStatusCode(200);
        response.setData(data);
        return response;
    }

    public static RpcResponse fail(String id, int serializerType) {
        RpcResponse response = new RpcResponse();
        response.setId(id);
        response.setSerializerType(serializerType);
        response.setStatusCode(500);
        return response;
    }
}
