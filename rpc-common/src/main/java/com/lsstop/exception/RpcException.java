package com.lsstop.exception;

/**
 * rpc调用异常
 *
 * @author lss
 * @date 2022/08/17
 */
public class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
