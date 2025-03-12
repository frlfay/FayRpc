package com.ff.exp.common.model;

import java.io.Serializable;

/**
 * ClassName: User
 * Package: com.ff.exp.common.model
 * Description: 用户实体类User
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:02
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
