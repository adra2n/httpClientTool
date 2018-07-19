package com.taolc.http;

public class DwkApiTest {

    public static void main(String[] args) {
        String url = "http://localhost:8888/dwkapi/swUser/exportSwUser.do";
        String response = HttpClientTool.post(url);
        System.out.println(response);
    }
}
