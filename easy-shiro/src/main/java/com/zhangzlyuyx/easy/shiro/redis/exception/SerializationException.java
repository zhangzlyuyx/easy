package com.zhangzlyuyx.easy.shiro.redis.exception;

public class SerializationException extends Exception {
    public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
