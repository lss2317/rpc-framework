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

    int FASTJSON_SERIALIZER = 1;
    int JACKSON_SERIALIZER = 1 << 1;
    int KRYO_SERIALIZER = 1 << 2;


    static CommonSerializer getSerializerByCode(int code) {
        switch (code) {
            case FASTJSON_SERIALIZER:
                return new FastJsonSerializer();
            case JACKSON_SERIALIZER:
                return new JsonSerializer();
            case KRYO_SERIALIZER:
                return new KryoSerializer();
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
