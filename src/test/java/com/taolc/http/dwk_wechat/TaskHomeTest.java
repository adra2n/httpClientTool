package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 新品体验接口调用
 */
public class TaskHomeTest {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1; i++) {
            pool.submit(() -> {
                String url = "http://localhost:8080/tasks/home";
                String response = HttpClientTool.get(url);
                System.out.println(response);
            });
        }
        pool.shutdown();
    }
}
