package com.lsstop.spring;

import com.lsstop.annotation.RpcResource;
import com.lsstop.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Spring Bean 后处理器
 *
 * @author lss
 * @date 2022/09/06
 */
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final RpcClientProxy rpcClientProxy;

    public SpringBeanPostProcessor(RpcClientProxy rpcClientProxy) {
        this.rpcClientProxy = rpcClientProxy;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RpcResource.class)) {
                Object proxy = rpcClientProxy.getProxy(field.getType());
                //无视私有
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
