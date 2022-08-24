package com.lsstop.utils;

import com.lsstop.entity.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author lss
 * @date 2022/08/20
 */
public class RedisUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 默认ip
     */
    public static final String DEFAULT_HOST = "localhost";

    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 6379;


    private static Jedis jedis;

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param url         配置信息
     */
    public static void registerService(String serviceName, URL url) {

    }


    private static void init() {
        if (jedis == null) {
            synchronized (RedisUtil.class) {
                if (jedis == null) {
                    jedis = getJedis();
                }
            }
        }
    }

    private static Jedis getJedis() {
        //获取配置文件redis设置
        return new Jedis(DEFAULT_HOST, DEFAULT_PORT);
    }
}
