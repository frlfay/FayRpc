package com.ff.ffrpc.serializer;

import java.io.IOException;

/**
 * ClassName: Serializer
 * Package: com.ff.ffrpc.serializer
 * Description: 序列化器接口
 * 提供序列化和反序列化两个方法，便于后续扩展更多的序列化器
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 20:36
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
