package com.lsstop.serializable;

import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;

/**
 * 序列化接口
 *
 * @author lss
 * @date 2022/08/17
 */
public interface CommonSerializer {

    Integer FASTJSON_SERIALIZER = 1;
    Integer JACKSON_SERIALIZER = 1 << 1;


    static CommonSerializer getSerializerByCode(int code) {
        switch (code) {
            case 1:
                return new FastJsonSerializer();
            case 1 << 1:
                return new JsonSerializer();
            default:
                throw new RpcException(RpcErrorEnum.SERIALIZER_NOT_FOUND);
        }
    }

    /**
     * 序列化
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     */
    Object deserialize(byte[] bytes, Class<?> clazz);

    /**
     * 获取序列号code
     *
     * @return code
     */
    int getSerializeCode();
}
