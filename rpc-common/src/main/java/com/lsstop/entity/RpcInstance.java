package com.lsstop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lss
 * @date 2022/08/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcInstance {

    /**
     * ip地址
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 权重
     */
    private int weight;

    /**
     * 是否健康（在线）
     */
    private boolean health;
}
