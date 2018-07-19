package com.taolc.http;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpClientToolTest {

    public static void main(String[] args) {
        post();
    }

    public static void post(){
        ExecutorService pool = Executors.newFixedThreadPool(20);
        for(int i=0;i<1;i++){
            pool.submit(() -> {
                HttpClientTool.get("http://localhost:8080/http/get?name=taolc");
                Map<String, String> map = new HashMap<>();
                map.put("name","taolc");
                map.put("sex","男");
                map.put("age","28");
                HttpClientTool.get("http://localhost:8080/http/get",map);

                HttpClientTool.post("http://localhost:8080/http/post",map);
                HttpClientTool.postJson("http://localhost:8080/http/post",JSONArray.toJSONString(map));
            });
        }
        pool.shutdown();
    }
}
