package com.lsstop.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author lss
 * @date 2022/09/09
 */
public class KryoSerializer implements CommonSerializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(KryoSerializer.class);

    // 由于kryo不是线程安全的，所以每个线程都使用独立的kryo
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        //支持对象循环引用（否则会栈溢出）
        kryo.setReferences(true);
        //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);
            Kryo kryo = kryoLocal.get();
            kryo.writeObject(output, obj);
            return output.toBytes();
        } catch (Exception e) {
            LOGGER.info("序列化异常:{}", e.getMessage());
            throw new SerializeException("序列化异常");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);
            Kryo kryo = kryoLocal.get();
            return kryo.readObject(input, clazz);
        } catch (Exception e) {
            LOGGER.info("反序列化异常:{}", e.getMessage());
            throw new SerializeException("反序列化异常");
        }
    }

    @Override
    public int getSerializeCode() {
        return KRYO_SERIALIZER;
    }
}
