package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;

public class ParamterTest {
    public static void main(String[] args) {
        String url = "http://localhost:8080/redis/test";
        String json = "{\"ob-token\":\"cc\",\"demo\":{\"name\":\"aa\",\"value\":\"bb\"}}";
        HttpClientTool.postJson(url,json);
    }
}
