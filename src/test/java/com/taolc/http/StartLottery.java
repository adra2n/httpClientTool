package com.taolc.http;

import org.springframework.util.StopWatch;

public class StartLottery {

    public static void main(String[] args) {

        for(int i=0;i<100;i++){
            new Thread(() -> {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                String url = "http://localhost:8080/productOrders/startLottery.do";
                HttpClientTool.post(url);
                stopWatch.stop();
                System.out.println("花费时间 --> " + stopWatch.getTotalTimeMillis());
            }).start();
        }
    }
}
