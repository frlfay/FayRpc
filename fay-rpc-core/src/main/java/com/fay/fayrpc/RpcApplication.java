package com.fay.fayrpc;

/**
 * ClassName: RpcApplication
 * Package: com.fay.fayrpc
 * Description: RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 *
 * @Author: FLFfang
 * @Create: 2025/4/18 - 01:22
 */

import com.fay.fayrpc.config.RpcConfig;
import com.fay.fayrpc.constant.RpcConstant;
import com.fay.fayrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    /**
     * 禁止指令重排序，确保多线程环境下 rpcConfig 的可见性。
     * 防止未初始化的对象被其他线程访问（避免“半初始化”问题）
     */
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持用户传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
    }

    /**
     * 初始化
     * 从配置文件（如 application.properties）加载配置，失败时使用默认构造的配置对象
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置(双检锁逻辑)
     * 加锁范围：仅对初始化过程同步，后续访问无需锁
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {                  // 第一次检查（避免不必要的加锁（性能优化）
            synchronized (RpcApplication.class) { // 加锁
                if (rpcConfig == null) {         // 第二次检查（防止多个线程同时通过第一次检查后重复初始化）
                    init();                       // 初始化配置
                }
            }
        }
        return rpcConfig;
    }
}

