package com.lsstop.serializable;

/**
 * 序列化接口
 *
 * @author lss
 * @date 2022/08/17
 */
public interface CommonSerializer {

    Integer JACKSON_SERIALIZER = 0;
    Integer DEFAULT_SERIALIZER = JACKSON_SERIALIZER;

    /**
     * 序列化
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     */
    Object deserialize(byte[] bytes, Class<?> clazz);
}
