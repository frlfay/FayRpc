package com.fay.fayrpc.config;

import lombok.Data;

/**
 * ClassName: RpcConfig
 * Package: com.fay.fayrpc.config
 * Description: 用于保存配置信息
 *
 * @Author: FLFfang
 * @Create: 2025/4/15 - 20:31
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "fay-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

}
