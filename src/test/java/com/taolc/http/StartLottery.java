package com.taolc.http;

public class StartLottery {

    public static void main(String[] args) {
        String url = "http://localhost:8080/productOrders/startLottery.do";
        String response = HttpClientTool.post(url);
        System.out.println(response);
    }
}
