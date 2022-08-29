package com.lsstop.serializable;

import com.alibaba.fastjson.JSON;
import com.lsstop.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fastjson序列化
 *
 * @author lss
 * @date 2022/08/26
 */
public class FastJsonSerializer implements CommonSerializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(FastJsonSerializer.class);

    @Override
    public byte[] serialize(Object obj) {
        try {
            return JSON.toJSONBytes(obj);
        } catch (Exception e) {
            LOGGER.info("序列化异常:{}", e.getMessage());
            throw new SerializeException("序列化异常");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            return JSON.parseObject(bytes, clazz);
        } catch (Exception e) {
            LOGGER.info("反序列化异常:{}", e.getMessage());
            throw new SerializeException("反序列化异常");
        }
    }

    @Override
    public int getSerializeCode() {
        return FASTJSON_SERIALIZER;
    }
}
