package com.ff.exp.provider;

import com.ff.exp.common.service.UserService;
import com.ff.ffrpc.registry.LocalRegistry;
import com.ff.ffrpc.server.HttpServer;
import com.ff.ffrpc.server.VertxHttpServer;

/**
 * ClassName: EasyProviderExample
 * Package: com.ff.exp.provider
 * Description: 简易服务提供者示例
 * 之后会在 main 方法中编写提供服务的代码
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 18:12
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
