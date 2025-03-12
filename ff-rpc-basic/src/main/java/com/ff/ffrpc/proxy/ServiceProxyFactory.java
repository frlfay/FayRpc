package com.ff.ffrpc.proxy;

import java.lang.reflect.Proxy;

/**
 * ClassName: ServiceProxyFactory
 * Package: com.ff.ffrpc.proxy
 * Description: 服务代理工厂（用于创建代理对象）
 *
 * @Author: FLFfang
 * @Create: 2025/3/12 - 21:44
 */

public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}

