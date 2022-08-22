package com.lsstop.serializable;

/**
 * 序列化接口
 *
 * @author lss
 * @date 2022/08/17
 */
public interface CommonSerializer {

    Integer JACKSON_SERIALIZER = 1;
    Integer KRYO_SERIALIZER = 1 << 1;
    Integer DEFAULT_SERIALIZER = JACKSON_SERIALIZER;


    static CommonSerializer getSerializerByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            case 2:
                return null;
            default:
                return null;
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
