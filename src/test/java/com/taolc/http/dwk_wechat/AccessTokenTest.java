package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccessTokenTest {
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenTest.class);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for(int i=0;i<30;i++){
            pool.submit(() -> {
                String url = "http://localhost:8080/adminext/getAccessToken.do";
                String response = HttpClientTool.post(url);
                logger.info(response);
            });
        }
        pool.shutdown();
    }

}
