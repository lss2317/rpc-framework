package com.lsstop.entity;

import lombok.*;

import java.util.Objects;

/**
 * @author lss
 * @date 2022/08/17
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private int weight = 1;

    /**
     * 健康状况
     */
    private boolean healthy = true;

    public URL(String serviceName, String host, int port, int weight) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return port == url.port && weight == url.weight && serviceName.equals(url.serviceName) && host.equals(url.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, host, port, weight);
    }
}
