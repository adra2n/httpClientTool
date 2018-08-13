package com.taolc.http.loanStream;

import com.taolc.http.HttpClientTool;
import com.taolc.http.util.MD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/7/22.
 */
public class LoanStreamTest {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1; i++) {
            pool.submit(() -> {
                String phone = "13681975404";
                String url = "http://localhost:8080/loanStream/api/sendPhoneCode";
                Map<String,String> map = new HashMap<>();
                map.put("phone",phone);
                map.put("type","1");
                Long timestamp = System.currentTimeMillis();
                String salt = "abcdefg123456";
                map.put("timestamp",timestamp + "");
                map.put("sign", MD5Util.md5(salt + timestamp));

                String response = HttpClientTool.get(url,map);
                System.out.println(response);
            });
        }
        pool.shutdown();
    }

    /**
     * 获取length长度的随机数字
     * @param length
     * @return
     */
    public static String getRandomNumber(int length){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<length;i++){
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
