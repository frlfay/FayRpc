package com.fay.exp.consumer;

import com.fay.exp.common.model.User;
import com.fay.exp.common.service.UserService;
import com.fay.fayrpc.proxy.ServiceProxyFactory;

/**
 * ClassName: EasyConsumerExample
 * Package: com.fay.exp.consumer
 * Description: 简易服务消费者示例
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:14
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // // 静态代理
        // UserService userService = new UserServiceProxy();

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        // todo 需要获取 UserService 的实现类对象
        // UserService userService = null;
        User user = new User();
        user.setName("fay");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
