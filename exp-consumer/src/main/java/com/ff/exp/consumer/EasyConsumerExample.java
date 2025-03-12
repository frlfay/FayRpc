package com.ff.exp.consumer;

import com.ff.exp.common.model.User;
import com.ff.exp.common.service.UserService;

/**
 * ClassName: EasyConsumerExample
 * Package: com.ff.exp.consumer
 * Description: 简易服务消费者示例
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:14
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // todo 需要获取 UserService 的实现类对象
        UserService userService = null;
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
