package com.taolc.http.RPC;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;

import java.util.HashMap;
import java.util.Map;

public class RpcJpaDemo {

    public static String uri = "http://localhost:8080/";

    public static void main(String[] args) {
        findAll();
    }

    public static void findAll(){
        String response = HttpClientTool.post(uri + "findAll");
        System.out.println(response);
    }

    public static void insert(){
        Map<String,String> map = new HashMap<>(3);
        map.put("name","11");
        map.put("address","22");
        String response = HttpClientTool.postJson(uri + "save",JSONArray.toJSONString(map));
    }

}
