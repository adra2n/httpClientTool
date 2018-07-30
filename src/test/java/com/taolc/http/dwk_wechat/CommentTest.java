package com.taolc.http.dwk_wechat;

import com.taolc.http.HttpClientTool;
import com.taolc.http.util.FileUtil;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommentTest {

    public static void main(String[] args) {
        writeComment();
    }

    public static void writeComment(){
        BufferedReader bufferedReader;
        try {
            bufferedReader = FileUtil.getFileBufferedReader("D:\\comment.txt");
            String line;
            while((line=bufferedReader.readLine())!=null){
                String[] array = line.split("</p>");
                Arrays.stream(array).forEach(item -> {
                    //只保留中文和字符
                    String reg = "[^\u4e00-\u9fa5|\\，||！||？||。]";
                    item = item.replaceAll(reg, "");
                    if(!StringUtils.isEmpty(item)){
//                        System.out.println(item);
                        Map<String,String> headers = new HashMap<>();
                        headers.put("Cookie","JSESSIONID=40972C64E9A38DB1457ECD1CD850D7C2");

                        String json = "{\"reviewContent\":\""+item+"\",\"topicId\":\"HT10079\",\"imageUrl\":\"\",\"ob-token\":\"\\\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOjIwMDAzMjUsImFjY291bnROYW1lIjoi6qeB4Ly6IOC8u-qngiIsImFjY291bnRUeXBlIjoiVVNFUiIsImV4cCI6MTUzMzAyNjgzMCwiaWF0IjoxNTMyOTQwNDMwfQ.Ds0JLdTkaJboex6TgHCgVdyq1BmxoDb1-75YQO76qIc\\\"\"}";
                        String url = "http://test.qquser.mur.qq.com/mobile/topic/v2/writecomments";
                        String response = HttpClientTool.postJson(url,headers,json);
                        System.out.println(response);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
