package netty.study.bio.improvement.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* Description: 用于创建线程池
 * 使用线程池来限制线程的数量，使用消息队列
 * 有序处理客户端对象
* @author: yuhang tao
* @date: 2019/8/16
* @version: v1.0
*/

public class ImproTimeServerExecutePool {

    private ExecutorService executorService;

    public ImproTimeServerExecutePool(int maxPoolSize,int queueSize){
        executorService=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task){
        executorService.execute(task);
    }
}
