package com.taolc.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by taolc on 2017/06/08.
 */
public class GenerateCodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(GenerateCodeUtil.class);

    /**
     * 获取 时间戳 + code_len 随机数
     * @param length
     * @return
     */
    public static String generateCode(int length){
        return System.currentTimeMillis()+RandomUtil.getRandomNumber(length);
    }

    /**
     * (伪随机)生成32位uuid(它保证对在同一时空中的所有机器都是唯一的)
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * (伪随机)生成32位uuid(它保证对在同一时空中的所有机器都是唯一的)
     * @return
     */
    public static String generateFullUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 随机生成手机号码
     * @return
     */
    public static String generatePhone(){
        String[] prePhone = {"130","131","132","133","134","135","136","137","138","139","150","151","152","153","155","156","157","158","159","180","182","185","186","187","188","189"};//26
        return prePhone[RandomUtil.getRandom(0,25)] + RandomUtil.getRandomNumber(8);
    }

    /**
     * 随机生成英文
     * @param length
     * @return
     */
    public static String generateEngish(int length){
        if(length < 1){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for( int i = 0; i < length; i ++) {
            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
            stringBuilder.append((char)(choice + random.nextInt(26)));
        }
        return stringBuilder.toString();
    }

    /**
     * 随机生成中文
     * @param length
     * @return
     */
    public static String generateChinese(int length){
        if(length < 1){
            return null;
        }

        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();

        byte[] b = new byte[2*length];

        for(int i=0;i<length;i++){
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));

            b[i*2] = (Integer.valueOf(hightPos)).byteValue();
            b[i*2+1] = (Integer.valueOf(lowPos)).byteValue();
        }

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            logger.info("code error {}",e);
        }
        return str;
    }

    /**
     * 随机生成身份证号
     * @return
     */
    public static String generateIDCardNo(){
        return GenerateIDCard.getIDCardNo();
    }

}
