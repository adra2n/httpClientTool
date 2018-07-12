package com.taolc.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for(int i=0;i<1;i++){
            pool.submit(new DemoThread("https://blog.csdn.net/baidu_37107022/article/details/76572195"));
            pool.submit(new DemoThread("https://www.cnblogs.com/qingwen/p/5578862.html"));
        }
        pool.shutdown();
    }
}

class DemoThread implements Runnable{

    private String url;
    DemoThread(String url){
        this.url = url;
    }

    @Override
    public void run() {
        String response = HttpClientTool.post(url);
        System.out.println(response);
        System.out.println();
    }
}
