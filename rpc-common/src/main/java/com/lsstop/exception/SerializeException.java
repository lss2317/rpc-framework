package com.lsstop.exception;

/**
 * @author lss
 * @date 2022/08/18
 */
public class SerializeException extends RuntimeException{

    public SerializeException() {
        super();
    }

    public SerializeException(String message) {
        super(message);
    }
}
