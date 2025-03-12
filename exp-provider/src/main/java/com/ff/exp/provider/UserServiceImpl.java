package com.ff.exp.provider;

import com.ff.exp.common.model.User;
import com.ff.exp.common.service.UserService;

/**
 * ClassName: UserServiceImpl
 * Package: com.ff.exp.provider
 * Description: 实现公共模块中定义的用户服务接口。
 * 功能是打印用户的名称，并且返回参数中的用户对象。
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:08
 */
public class UserServiceImpl implements UserService {

    public User getUser(User user){
        System.out.println("用户名:" + user.getName());
        return user;
    }
}
