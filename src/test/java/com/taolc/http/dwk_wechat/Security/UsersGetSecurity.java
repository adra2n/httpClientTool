package com.taolc.http.dwk_wechat.Security;

import com.taolc.http.HttpClientTool;

import java.util.HashMap;
import java.util.Map;

public class UsersGetSecurity {
    public static void main(String[] args) {
        String url = "http://test.qquser.mur.qq.com/users/get.do";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjIwMDAyODMsImFjY291bnROYW1lIjoi56mG5LyY56eA5bCP5pyL5Y-LIiwiYWNjb3VudFR5cGUiOiJVU0VSIiwiZXhwIjoxNTM0Mzk4NDc2LCJpYXQiOjE1MzQzMTIwNzZ9.4V77ExgSzF07TzLZPKuhvG2PBiVe7nitv44bDgNOJGQ";
        Map<String,String> headers = new HashMap<>();
        headers.put("ob-token",token);
        Map<String,String> params = new HashMap<>();
        params.put("aLong","2");
        params.put("ob-token",token);
        String reponseString = HttpClientTool.post(url,headers,params);
        System.out.println(reponseString);
    }
}
