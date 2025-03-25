# basic-rpc框架开发

## I. 什么是RPC？

- 专业定义：RPC（Remote Procedure Call）即远程过程调用，是一种计算机通信协议。
- 它允许程序在不同的计算机之间 进行通信和交互，就像本地调用一样。
- 本身不是一种协议，而是一种调用。
- 常用的 RPC 协议实现：
  - gRPC
  - thrift

## II. 为什么需要RPC？

- 开发者使用 RPC 框架，可以轻松调用远程服务，快速开发分布式系统；
- 而不需要了解数据的传输处理过程、底层网络通信的细节等。
- 比如之前要买离家有一定距离的东西要自己跑腿到线下店去购买，现在的话我们可以使用外卖平台让骑手配送到家，不需要关心网络怎么传输、骑手怎么配送的，只需要在外卖平台点点点就行。

## III. RPC框架实现思路

- 首先两个角色：我们有服务消费者，想要调用服务提供者的一个方法。
- 怎么调用呢：需要提供者启动一个【web服务器】，然后消费者通过请求客户端发送HTTP或者其它协议的请求来调用。
  - 比如：消费者要调用UserService服务的getUser方法
  - 消费者请求 fay.icu/getUser 地址后，提供者会调用UserService接口的getUser方法
- 但如果提供者提供了多个服务和方法的话，那每个接口和方法都要单独写一个接口？消费者对于每个接口都要编写调用吗？
  - 所以我们可以使用【请求处理器】提供一个统一的服务调用接口，根据客户端的请求参数来进行不同的处理、调用不同的服务和方法。
  - 可以在服务提供者程序维护一个【本地服务注册器】，记录服务和对应实现类的映射。
  - 还是上面那个例子：消费者要调用UserService服务的getUser方法，可以发送请求，参数为 `service=userService, method=getUser`，然后请求处理器会根据service从本地服务注册器中找到对应的服务实现类UserService，并通过Java的反射机制调用method指定的getUser方法。
- 【序列化器】需要注意的是，由于Java对象无法直接在网络中传输，所以需要对传输的参数进行序列化和反序列化。
- 为了简化消费者请求的代码，实现类似本地调用的体验。可以基于代理模式，为消费者要调用的接口生成一个【代理对象】，由代理对象完成请求和响应的过程。

## IV. 开发

#### 项目准备

1. 项目初始化
   - 创建根目录，根目录下创建四个maven module（下面三个+fay-rpc-basic）
2. 公共模块 exp-common 包括接口、Model
   - model.User(实现序列化接口，为后续网络传输序列化提供支持)
   - service.UserService 提供获取用户的方法
3. 服务提供者 exp-provider
   - 引入依赖：fay-rpc-basic、exp-common、hutool、lombok
   - 服务实现类：implements UserService -> 打印用户的名称并返回参数中的用户对象
   - 启动类：EasyProviderExample 之后在这里的main方法中编写提供服务的代码
4. 服务消费者 exp-consumer
   - 引入依赖：fay-rpc-basic、exp-common、hutool、lombok
   - 启动类：EasyConsumerExample 编写调用接口的代码。目前无法获取到userService实例，预留为null。我们的目标是通过rpc框架得到一个支持远程调用服务提供者的代理对象 -> 像调用本地方法一样调用userService的方法

#### web服务器

> - 让服务提供者 提供 **可远程访问** 的服务 -> 需要web服务器（接受处理请求并返回响应）
> - web服务器可选：Spring Boot 内嵌的 Tomcat、NIO 框架 Netty 和 Vert.x 等
> - 这里我们选择：高性能的 NIO 框架 Vert.x

- 在模块`fay-rpc-basic`中
- 引入依赖：vertx-core、hutool、lombok
- server包 编写接口 HttpServer 用于启动服务器 void doStart(int port); -> 定义统一的启动服务器方法便于后续扩展，例如实现多种不同的web服务器
- 编写 VertxHttpServer implements HttpServer，监听指定端口并处理请求
- 验证是否启动成功：
  - 在 EasyProviderExample 编写启动web服务的代码 httpServer.doStart(8080); 
  - 访问 localhost:8080 

#### 本地服务注册器

> basic不用第三方注册中心，注册到服务提供者本地即可 -> 本地服务注册器 LocalRegistry

- 在模块`fay-rpc-basic`中，新建registry包 编写LocalRegistry
- 使用线程安全的 ConcurrentHashMap 存储服务注册信息，key为服务名称，String类型； value为服务的实现类，Class<?>
  - 可以根据要调用的服务名称获取到对应的实现类，然后通过反射进行方法调用
- 修改 EasyProviderExample -> 增加注册服务的代码。先注册服务后启动web服务

#### 序列化器

> 因为无论是请求或响应，都会设计参数的传输。而Java对象是存活在JVM虚拟机中的，如果想在其他位置存储并访问、或者在网络中进行传输，就需要进行序列化和反序列化
>
> 序列化：将Java 对象转沩可传输的字节数组。 反序列化：将字节数组转换为 Java 对象
>
> 有很多种不同的序列化方式，比如Java 原生序列化、JSON、Hessian、Kryo、protobuf 等

- 选择Java原生序列化器

- 在模块`fay-rpc-basic`中，新建serializer包，编写序列化接口Serializer，提供序列化反序列化两个方法，便于后续扩展更多的序列化器

  ```java
  import java.io.IOException;
  
  /**
   * 序列化器接口
   */
  public interface Serializer {
  
      /**
       * 序列化
       *
       * @param object
       * @param <T>
       * @return
       * @throws IOException
       */
      <T> byte[] serialize(T object) throws IOException;
  
      /**
       * 反序列化
       *
       * @param bytes
       * @param type
       * @param <T>
       * @return
       * @throws IOException
       */
      <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
  }
  ```

- 编写JdkSerializer implements Serializer（无需记忆）

  ```java
  import java.io.*;
  
  /**
   * JDK 序列化器
   */
  public class JdkSerializer implements Serializer {
  
      /**
       * 序列化
       *
       * @param object
       * @param <T>
       * @return
       * @throws IOException
       */
      @Override
      public <T> byte[] serialize(T object) throws IOException {
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
          objectOutputStream.writeObject(object);
          objectOutputStream.close();
          return outputStream.toByteArray();
      }
  
      /**
       * 反序列化
       *
       * @param bytes
       * @param type
       * @param <T>
       * @return
       * @throws IOException
       */
      @Override
      public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
          ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
          ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
          try {
              return (T) objectInputStream.readObject();
          } catch (ClassNotFoundException e) {
              throw new RuntimeException(e);
          } finally {
              objectInputStream.close();
          }
      }
  }
  ```

#### 提供者处理调用 - 请求处理器

> 请求处理器是 RPC框架的实现关键，它的作用是：处理接收到的请求，并根据请求参数找到对应的服务和方法，通过反射实现调用，最后封装返回结果并响应请求

- 在模块`fay-rpc-basic`中，新建model包，编写请求和响应封装类 RpcRequest、RpcResponse
  - 请求类 RpcRequest 的作用是封装调用所需的信息， 比如服务名称、方法名称、调用参数的类型列表、参数列表。这些都是 Java 反射机制所需的参数。
  - 响应类 RpcResponse 的作用是封装调用方法得到的返回值、以及调用的信息（比如异常情况）等
- 在server包编写请求处理器 HttpServerHandler
  - 1.反序列化请求为对象，并从请求对象中获取参数。 
  - 2.根据服务名称从本地注册器中获取到对应的服务实现类。 
  - 3.通过反射机制调用方法，得到返回结果。 
  - 4.对返回结果进行封装和序列化，并写入到响应中。
  - 需要注意，不同的 web 服务器对应的请求处理器实现方式也不同，比如 Vert.x 中是通过实现 Handler<HttpServerRequest> 接口来自定义请求处理器的。并且可以通过 request.bodyHandler 异步处理请求。
- 给HttpServer绑定请求处理器
  - 修改 VertxHttpServer 的代码，通过 server.requestHandler 绑定请求处理器。
  - 至此已经引入了RPC框架的服务提供者模块，能够接受请求并完成服务调用了。

#### 消费方发起调用- 代理

> 在项目准备阶段，我们已经预留了一段调用服务的代码，只要能够获取到 UserService 对象（实现类）就能跑通整个流程。 
>
> 但 UserService 的实现类从哪来呢？ 总不能把服务提供者的 UserServicelmpl 复制粘贴到消费者模块吧？要能那样做还需要 RPC框架干什么？
>
> 分布式系统中，我们调用其他项目或团队提供的接口时，一般只关注请求参数和响应结果，而不关注具体实现。 
>
> 因此，我们可以通过生成代理对象来简化消费方的调用。 代理的实现方式大致分为2类：静态代理和动态代理，下面依次实现。

- ##### 静态代理

  - 静态代理是指为每一个特定类型的接口或对象，编写一个代理类。 
  - 比如在 example-consumer 模块中，创建一个静态代理 UserServiceProxy，实现 UserService 接口和 getUser 方法。
  - 只不过实现 getUser 方法时，不是复制粘贴服务提供者 UserServicelmpl 中的代码，而是要构造 HTTP 请求去调用服务提供者。 需要注意发送请求前要将参数序列化。
  - 然后修改 EasyConsumerExample，new 一个代理对象并赋值给 userService，就能完成调用。
  - 但是静态代理需要我们给每个服务接口都写一个实现类，很麻烦灵活性差，所以我们在PRC框架中使用动态代理

- ##### 动态代理

  - 作用是：根据要生成的对象的类型，自动生成一个代理对象
  - 我们使用JDK动态代理
  - 在模块`fay-rpc-basic`中，新建proxy包，编写动态代理类 ServiceProxy，需要实现 InvocationHandler 接口的 invoke 方法
  - 在模块`fay-rpc-basic`中，proxy包编写ServiceProxyFactory，作用是根据指定类创建动态代理对象
    - 这里使用工厂设计模式，来简化对象的创建过程。通过Proxy.newProxyInstance方法为指定类型创建对象
  - 最后在 EasyConsumerExample 中，就可以通过调用工厂来为 UserService 获取动态代理对象了
