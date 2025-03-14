package com.fay.exp.common.service;

import com.fay.exp.common.model.User;

/**
 * ClassName: UserService
 * Package: com.fay.exp.common.service
 * Description: 用户服务
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:03
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);
}


