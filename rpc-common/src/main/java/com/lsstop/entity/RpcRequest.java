package com.lsstop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * rpc请求信息实体
 *
 * @author lss
 * @date 2022/08/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求id
     */
    private String id;

    /**
     * 注册服务名称
     */
    private String name;

    /**
     * 请求接口名称
     */
    private String interfaceName;

    /**
     * 请求调用方法
     */
    private String method;

    /**
     * 方法调用参数
     */
    private Object[] args;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] argsType;

    /**
     * 负载均衡算法
     */
    private Integer balance;

    /**
     * 是否是响应数据
     */
    private Boolean isResponse;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

    /**
     * 是否是连接
     */
    private Boolean connect;

}
