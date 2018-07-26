package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccessTokenTest {
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenTest.class);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        int index = 1;
        for(int i=0;i<30;i++){
            pool.submit(() -> {
                String url = "http://localhost:8080/adminext/getAccessToken.do";
                String response = HttpClientTool.post(url);
                logger.info(response);
            });

            if(index % 10 == 0){
                try {
                    TimeUnit.SECONDS.sleep(31);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        pool.shutdown();
    }

}
