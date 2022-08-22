package com.lsstop.exception;

import com.lsstop.enums.ResponseEnum;
import com.lsstop.enums.RpcErrorEnum;

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

    public RpcException(RpcErrorEnum rpcErrorEnum){
        super(rpcErrorEnum.getMessage());
    }

    public RpcException(ResponseEnum responseEnum){
        super(responseEnum.getMessage());
    }
}
