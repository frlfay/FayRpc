package com.fay.exp.consumer;

import com.fay.fayrpc.config.RpcConfig;
import com.fay.fayrpc.utils.ConfigUtils;

/**
 * ClassName: ConsumerExample
 * Package: com.fay.exp.consumer
 * Description:
 *
 * @Author: FLFfang
 * @Create: 2025/4/18 - 01:46
 */
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
