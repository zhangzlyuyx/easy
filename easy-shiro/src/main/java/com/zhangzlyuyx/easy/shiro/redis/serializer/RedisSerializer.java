package com.zhangzlyuyx.easy.shiro.redis.serializer;

import com.zhangzlyuyx.easy.shiro.redis.exception.SerializationException;

public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
}
