package com.fay.exp.provider;

import com.fay.exp.common.service.UserService;
import com.fay.fayrpc.RpcApplication;
import com.fay.fayrpc.registry.LocalRegistry;
import com.fay.fayrpc.server.HttpServer;
import com.fay.fayrpc.server.VertxHttpServer;

/**
 * ClassName: ProviderExample
 * Package: com.fay.exp.provider
 * Description:
 *
 * @Author: FLFfang
 * @Create: 2025/4/18 - 01:49
 */
public class ProviderExample {
    public static void main(String[] args) {
        RpcApplication.init();

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
