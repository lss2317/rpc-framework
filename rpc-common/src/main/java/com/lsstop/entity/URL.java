package com.lsstop.entity;

import lombok.*;

/**
 * @author lss
 * @date 2022/08/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class URL {

    /**
     * 服务注册名称
     */
    private String serviceName;

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
