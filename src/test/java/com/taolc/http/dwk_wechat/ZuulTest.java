package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;

public class ZuulTest {
    public static void main(String[] args) {
        String url = "http://localhost:8080/rpc/swLetter/letter.do";
        HttpClientTool.get(url);
        HttpClientTool.post(url);
    }
}
