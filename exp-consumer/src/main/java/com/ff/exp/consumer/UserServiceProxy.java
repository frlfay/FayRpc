package com.ff.exp.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ff.exp.common.model.User;
import com.ff.exp.common.service.UserService;
import com.ff.ffrpc.model.RpcRequest;
import com.ff.ffrpc.model.RpcResponse;
import com.ff.ffrpc.serializer.JdkSerializer;
import com.ff.ffrpc.serializer.Serializer;
import java.io.IOException;
/**
 * ClassName: UserServiceProxy
 * Package: com.ff.exp.consumer
 * Description: 静态代理
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 21:34
 */
public class UserServiceProxy implements UserService {

    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
