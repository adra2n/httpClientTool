package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;

public class SecurityTest {

    public static void main(String[] args) {
        String url = "http://api.user.mur.qq.com/autoconfig";
        String getResponse = HttpClientTool.get(url);
        String postResponse = HttpClientTool.post(url);
        System.out.println(getResponse);
        System.out.println(postResponse);
    }
}
