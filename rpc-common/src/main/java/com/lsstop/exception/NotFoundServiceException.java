package com.lsstop.exception;

/**
 * @author lss
 * @date 2022/08/18
 */
public class NotFoundServiceException extends RuntimeException{
    public NotFoundServiceException(String message) {
        super(message);
    }
}
