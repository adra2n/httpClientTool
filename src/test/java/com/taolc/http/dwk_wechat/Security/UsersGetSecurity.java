package com.taolc.http.dwk_wechat.Security;

import com.taolc.http.HttpClientTool;

import java.util.HashMap;
import java.util.Map;

public class UsersGetSecurity {
    public static void main(String[] args) {
//        String url = "http://test.qquser.mur.qq.com/users/get.do";
//        String url = "http://api.user.mur.qq.com/users/get.do";
        String url = "http://wechat.user.mur.qq.com/users/get.do";
        String token;
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjIyODg4MTQsImFjY291bnROYW1lIjoi6qeB4Ly6IOC8u-qngiIsImFjY291bnRUeXBlIjoiVVNFUiIsImV4cCI6MTUzNDgxODY4NCwiaWF0IjoxNTM0NzMyMjg0fQ.9dTz7TupUPeZUYI3EkKn2bYWg7pXMhCROnNl3YQezYE";
        Map<String,String> headers = new HashMap<>();
        headers.put("ob-token",token);
        Map<String,String> params = new HashMap<>();
        params.put("aLong","33333");
        params.put("ob-token",token);
        String reponseString = HttpClientTool.post(url,headers,params);
        System.out.println(reponseString);
    }
}
