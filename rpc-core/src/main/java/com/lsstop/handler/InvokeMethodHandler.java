package com.lsstop.handler;

import com.lsstop.entity.RpcRequest;
import com.lsstop.enums.ResponseEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.provider.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 调用服务方法处理器
 *
 * @author lss
 * @date 2022/08/18
 */
public class InvokeMethodHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(InvokeMethodHandler.class);


    /**
     * 服务端调用方法服务
     *
     * @return 结果
     */
    public Object invokeTargetMethod(RpcRequest request) {
        Object result;
        try {
            //获取方法实现类
            Object service = ServiceProvider.getService(request.getInterfaceName());
            if (service == null) {
                throw new RpcException(ResponseEnum.CLASS_NOT_FOUND);
            }
            Method method = service.getClass().getMethod(request.getMethod(), request.getArgsType());
            result = method.invoke(service, request.getArgs());
            LOGGER.info("服务{} 成功调用方法{}", request.getInterfaceName(), request.getMethod());
        } catch (Exception e) {
            LOGGER.error("方法调用错误");
            throw new RpcException(ResponseEnum.METHOD_NOT_FOUND);
        }
        return result;
    }

}
