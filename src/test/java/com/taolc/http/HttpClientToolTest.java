package com.taolc.http;

public class HttpClientToolTest {

    private static String host = "localhost";
    private static int port = 8080;

    public static void main(String[] args) {
        get();
    }

    public static void get(){
        String url = "http://"+host+":"+port+"/http/get";
        String response = HttpClientTool.get(url);
        System.out.println(response);
    }
}
