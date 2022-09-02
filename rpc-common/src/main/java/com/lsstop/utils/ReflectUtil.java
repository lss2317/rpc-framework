package com.lsstop.utils;

/**
 * 反射工具类
 *
 * @author lss
 * @date 2022/09/02
 */
public class ReflectUtil {


    /**
     * 获取主启动类类名
     */
    public static String getMainClassName() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }


}
