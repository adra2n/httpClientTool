package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;

/**
 * redis 连接测试
 */
public class RedisConnectTest {

    public static void main(String[] args) {
//        devConnectTest();
        testConnectTest();
//        prodQQConnectTest();
//        prodWechatConnectTest();
    }

    /**
     * 开发环境测试
     */
    public static void devConnectTest(){
        String url = "http://localhost:8080";
        http(url);
    }

    /**
     * 测试环境测试
     */
    public static void testConnectTest(){
        String url = "http://test.qquser.mur.qq.com";
        http(url);
    }

    /**
     * 微信正式环境测试
     */
    public static void prodWechatConnectTest(){
        String url = "http://api.user.mur.qq.com";
        http(url);
    }

    /**
     * QQ正式环境测试
     */
    public static void prodQQConnectTest(){
        String url = "http://api.qquser.mur.qq.com";
        http(url);
    }

    private static void http(String url){
        String putResponse = HttpClientTool.post(url + "/redis/put");
        System.out.println("向redis放值 --> " + putResponse);

        String getResponse = HttpClientTool.post(url + "/redis/get");
        System.out.println("获取redis中的值 --> " + getResponse);
    }
}
