package com.xsj.app.util;

/**
 * @PackageName:com.xsj.app.util
 * @Description:
 * @author:Xsj
 * @date 2020/4/23 0023 10:27
 */
public class threadpooltest {

 public static void main(String[] args) {
     long start=System.currentTimeMillis();

     Threads.cacheThreadPool();
     //Threads.fixTheadPoolTest();

    /* for (int i = 0; i < 1000000; i++){
         System.out.println("执行第"+i+"次");
     }*/

      long end=System.currentTimeMillis();
     System.out.println("程序运行时间： "+(end-start)+"ms");

 }
}
