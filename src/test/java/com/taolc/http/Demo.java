package com.taolc.http;

import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) {
        for(int i=0;i<1000000;i++){
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(HttpClientTool.getHttpClient());
        }
    }
}

class DemoThread implements Runnable{

    @Override
    public void run() {
        String url = "";
        String response = HttpClientTool.post(url);
    }
}
