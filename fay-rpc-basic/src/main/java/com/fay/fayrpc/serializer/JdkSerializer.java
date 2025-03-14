package com.fay.fayrpc.serializer;

import java.io.*;

/**
 * ClassName: JdkSerializer
 * Package: com.fay.fayrpc.serializer
 * Description: 基于 Java 自带的序列化器实现 JdkSerializer
 * 这段代码无需记忆，需要用到的时候照抄即可，关键是要理解序列化和反序列化的区别
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 20:39
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化 - 基于 JDK 动态代理
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        //创建一个字节数组输出流，用于存储序列化之后的数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        try {
            // 将对象写入输出流 (序列化)
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            // 返回序列化后的字节数组
            return outputStream.toByteArray();
        } finally {
            // 关闭输出流，释放资源
            objectOutputStream.close();
        }
    }

    /**
     * 反序列化 - 基于 JDK
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    @SuppressWarnings("all")
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        // 检查字节数组是否为空或长度为 0
        if (bytes == null || bytes.length == 0) {
            throw new IOException("字节数组为空，无法反序列化");
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}