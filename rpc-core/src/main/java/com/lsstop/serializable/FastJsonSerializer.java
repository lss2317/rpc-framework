package com.lsstop.serializable;

import com.alibaba.fastjson.JSON;

/**
 * fastjson序列化
 *
 * @author lss
 * @date 2022/08/26
 */
public class FastJsonSerializer implements CommonSerializer {


    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        return JSON.parseObject(bytes, clazz);
    }

    @Override
    public int getSerializeCode() {
        return 1;
    }
}
