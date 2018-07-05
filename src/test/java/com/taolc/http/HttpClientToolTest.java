package com.taolc.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientToolTest {

    private static String host = "localhost";
    private static int port = 8080;

    public static void main(String[] args) {
//        get();
        post();
    }

    public static void get(){
        String url = "http://"+host+":"+port+"/http/get";
        String response = HttpClientTool.get(url);
        System.out.println(response);
    }

    public static void post(){
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i=0;i<10;i++){
            pool.submit(new PostThread(host,port));
        }
        pool.shutdown();
    }
}

class PostThread implements Runnable{
    private String host;
    private int port;

    PostThread(String host,int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        String url = "http://"+host+":"+port+"/http/post";
        String response = HttpClientTool.post(url);
        System.out.println(response);
    }
}
