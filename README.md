# Netty_Study
记录学习Netty的点点滴滴

##同步阻塞式I/O
最常见的就是我们使用*Socket*创建客户端与服务端，我们通常会在
服务端使用死循环并通过*accept*方法监听客户端连接

``while (true){
                  Socket accept = serverSocket.accept();
                  new Thread(new TraditionTimeServerHandle(accept)).start();
              }``

当接收到客户端连接请求后会为每个客户端创建一个新的线程进行链路处理。

__问题：__ 当客户并发访问量增加后，服务端的线程个数和客户端的并发访
问数呈1：1的正比关系，随着并发访问量的增大，系统会发生线程堆栈溢出、
创建线程失败等问题，最终导致进程宕机。

##伪异步I/O编程
