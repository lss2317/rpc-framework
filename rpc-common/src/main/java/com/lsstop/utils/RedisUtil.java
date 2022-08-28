package com.lsstop.utils;

import com.alibaba.fastjson.JSONArray;
import com.lsstop.entity.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lss
 * @date 2022/08/20
 */
public class RedisUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 默认ip
     */
    public static String DEFAULT_HOST = "localhost";

    /**
     * 默认端口
     */
    public static int DEFAULT_PORT = 6379;

    /**
     * redis密码
     */
    public static String PASSWORD = null;


    private static Jedis jedis;


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
        Jedis jedis = new Jedis(DEFAULT_HOST, DEFAULT_PORT);
        if (PASSWORD != null) {
            jedis.auth(PASSWORD);
        }
        return jedis;
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param url         服务信息
     */
    public static void addURL(String serviceName, URL url) {
        init();
        String urlList = jedis.get(serviceName);
        if (urlList == null) {
            List<URL> list = new ArrayList<>();
            list.add(url);
            String json = JSONArray.toJSONString(list);
            jedis.set(serviceName, json);
            return;
        }
        List<URL> list = JSONArray.parseArray(urlList, URL.class);
        list.add(url);
        String json = JSONArray.toJSONString(list);
        jedis.set(serviceName, json);
    }


    /**
     * 获取健康url集合
     *
     * @param serviceName 服务名称
     * @return list
     */
    public static List<URL> getURLList(String serviceName) {
        init();
        String json = jedis.get(serviceName);
        if (json == null) {
            return null;
        }
        return JSONArray.parseArray(json, URL.class).stream().filter(URL::isHealthy).collect(Collectors.toList());
    }

    /**
     * 服务下线
     * 将服务设置非健康
     */
    public static void offlineURL(String serviceName, URL url) {
        List<URL> urlList = getURLList(serviceName);
        if (urlList != null) {
            boolean remove = urlList.remove(url);
            if (remove) {
                url.setHealthy(false);
                urlList.add(url);
                jedis.set(serviceName, JSONArray.toJSONString(urlList));
            }
        }
    }

    public void setRedis(String host, int port) {
        setRedis(host, port, null);
    }

    public void setRedis(String host, int port, String password) {
        DEFAULT_HOST = host;
        DEFAULT_PORT = port;
        PASSWORD = password;
    }
}
