package com.ff.exp.common.service;

import com.ff.exp.common.model.User;

/**
 * ClassName: UserService
 * Package: com.ff.exp.common.service
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


