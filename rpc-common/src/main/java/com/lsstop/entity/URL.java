package com.lsstop.entity;

import lombok.*;

/**
 * @author lss
 * @date 2022/08/17
 */
@Data
@Builder
@ToString
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
    private double weight = 1.0D;

    /**
     * 健康状况
     */
    private boolean healthy = true;

    public URL(String serviceName, String host, int port, double weight) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.weight = weight;
    }
}
