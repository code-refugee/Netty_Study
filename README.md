# Netty_Study<br>
记录学习Netty的点点滴滴<br>

***注意在讨论IO时，一定要严格区分网络IO和磁盘文件IO，IO BLOCK是针对网络IO而言的***<br>

##同步阻塞式I/O<br>

最常见的就是我们使用*Socket*创建客户端与服务端，我们通常会在
服务端使用死循环并通过*accept*方法监听客户端连接<br>

``while (true){
                  Socket accept = serverSocket.accept();
                  new Thread(new TraditionTimeServerHandle(accept)).start();
              }``<br>

当接收到客户端连接请求后会为每个客户端创建一个新的线程进行链路处理。<br>

__问题：__ 当客户并发访问量增加后，服务端的线程个数和客户端的并发访
问数呈1：1的正比关系，随着并发访问量的增大，系统会发生线程堆栈溢出、
创建线程失败等问题，最终导致进程宕机。<br>

##伪异步I/O编程<br>

我们要如何解决同步阻塞式I/O遇到的问题呢？答案是通过创建*线程池*的方式<br>

``executorService=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                  maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));``<br>
              
通过线程池来限制系统创建线程的数量。但是由于我们的通信代码依然采用是阻塞式I/O，因此
无法从根本上解决问题。<br>

###什么是阻塞式和非阻塞式I/O？<br>
阻塞式I/O指的是”一旦输入/输出工作没有完成，则程序就会停在那里，直到输入/输出的工作完成“
具有代表性的如Java的 _InputStream_ 中的 _read()_ 方法，当我们用它对Socket的输入流进行读取
操作的时候，它会一直阻塞下去，直到发生如下三种事件：<br>
1、有数据可读<br>
2、可用数据已经读取完毕<br>
3、发送空指针或I/O异常<br>
非阻塞式I/O:并非完全非阻塞，通常是通过设置超时来读取数据的。未超时之前，程序阻塞在读写函数
上；超时后，结束本次读取，将读取到的数据返回。通过不断循环读取，就能够读到完整数据了。如果
多次连续超时读到空数据的话，则可以断开。<br>

##NIO（非阻塞I/O）<br>
NIO是指将IO模式设为“Non-Blocking”模式<br>
``socketChannel.configureBlocking(false);``<br>
在NIO模式下，如果调用read，如果发现数据没有到达，它是不会傻傻的等在那里，而是会去处理其它请求
过一会再来询问这里有没有数据。但这样做会有一个问题，如果这个“过一会”的时间设置太短，就会造成
过于频繁的重试，干耗CPU，时间设置太长又会加大程序响应延迟。<br>
通常我们NIO是与IO多路复用组合起来使用的。IO多路复用是这么一种机制：程序注册一组socket文件描述
符给操作系统，表示“我要监视这些fd是否有IO事件发生，有了就告诉程序处理”。它是一口气告诉程序，
哪些数据到了，而不再是一个一个去问。<br>
``socketChannel.register(selector, SelectionKey.OP_ACCEPT);``<br>
NIO和IO多路复用是两个相对独立的事情。NIO仅仅是指IO API总是能立刻返回，不会被Blocking；而IO多路
复用仅仅是操作系统提供的一种便利的通知机制<br>

##AIO（异步非阻塞I/O）<br>
由JDK底层的线程池负责回调并驱动读写操作，我们不需要像NIO编程那样创建一个独立的I/O线程来处理读写
操作

##NETTY<br>


##参考文献：<br>
[聊聊BIO，NIO和AIO](https://www.jianshu.com/p/ef418ccf2f7d)<br>