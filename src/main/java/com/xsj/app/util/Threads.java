package com.xsj.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程相关工具类.
 *
 * @author xsj
 */
public class Threads {
    private static final Logger logger = LoggerFactory.getLogger(Threads.class);

    /**
     * 适用：执行很多短期异步的小程序或者负载较轻的服务器
     * 1.创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲（60秒不执行任务）的线程
     * 2.当任务数增加时，此线程池又可以智能的添加新线程来处理任务
     * 3.此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小
     * 当有新任务到来，则插入到SynchronousQueue中，由于SynchronousQueue是同步队列，因此会在池中寻找可用线程来执行
     * 若有可以线程则执行，若没有可用线程则创建一个线程来执行该任务；若池中线程空闲时间超过指定大小，则该线程会被销毁。
     */
    public static void cacheThreadPool() {
        //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 1; i <= 8; i++) {
            final int ii = i;
            try {
                Thread.sleep(ii * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(() ->
                System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii)
            );
        }

        cachedThreadPool.shutdown();
    }

    /**
     * 适用：执行长期的任务，性能好很多
     * 1.创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小<br>
     * 2.线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程<br>
     * 3.因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字，和线程名称<br>
     * <p>
     * 创建可容纳固定数量线程的池子，每隔线程的存活时间是无限的，当池子满了就不在添加线程了
     * 如果池中的所有线程均在繁忙状态，对于新任务会进入阻塞队列中(无界的阻塞队列)
     */
    public static void fixTheadPoolTest() {
        long start=System.currentTimeMillis();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            fixedThreadPool.execute(() -> {
                System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

         long end=System.currentTimeMillis();
     System.out.println("程序运行时间： "+(end-start)+"ms");

    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行。延迟执行
     */
    public static void sceduleThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        Runnable r1 = () -> System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行:3秒后执行");
        scheduledThreadPool.schedule(r1, 3, TimeUnit.SECONDS);
        Runnable r2 = () -> System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行:延迟2秒后每3秒执行一次");
        scheduledThreadPool.scheduleAtFixedRate(r2, 2, 3, TimeUnit.SECONDS);
        Runnable r3 = () -> System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行:普通任务");
        for (int i = 0; i < 5; i++) {
            scheduledThreadPool.execute(r3);
        }
    }

    /**
     * 适用：一个任务一个任务执行的场景
     * 创建只有一个线程的线程池，且线程的存活时间是无限的；当该线程正繁忙时，对于新任务会进入阻塞队列中(无界的阻塞队列)
     */
    public static void singleTheadPoolTest() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + "=>" + ii));
        }
    }

    /**
     * sleep等待,单位为毫秒
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            return;
        }
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍人超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        logger.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 打印线程异常信息
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            logger.error(t.getMessage(), t);
        }
    }
}
