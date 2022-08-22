package com.lsstop.serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsstop.entity.RpcRequest;
import com.lsstop.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JACKSON序列化
 *
 * @author lss
 * @date 2022/08/18
 */
public class JsonSerializer implements CommonSerializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            LOGGER.info("序列化异常");
            throw new SerializeException("序列化异常");
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Object readValue = null;
        try {
            readValue = objectMapper.readValue(bytes, clazz);
            if (readValue instanceof RpcRequest){
                return handleRequest(readValue);
            }
            return readValue;
        } catch (IOException e) {
            LOGGER.info("反序列化异常");
        }
        return readValue;
    }

    @Override
    public int getSerializeCode() {
        return 1;
    }

    /**
     * 这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
     * 需要重新判断处理
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getArgsType().length; i++) {
            Class<?> clazz = rpcRequest.getArgsType()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getArgs()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getArgs()[i]);
                rpcRequest.getArgs()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }
}
