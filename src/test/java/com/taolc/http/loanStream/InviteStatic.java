package com.taolc.http.loanStream;

import com.taolc.db.DBConnect;
import com.taolc.db.query.QuerySQL;
import com.taolc.enums.DBEnum;
import com.taolc.http.HttpClientTool;
import com.taolc.http.util.GenerateCodeUtil;
import com.taolc.http.util.MD5Util;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/19.
 * 渠道推广统计
 */
public class InviteStatic {

    public static void main(String[] args) {
        for(int j=0;j<20;j++){
            for(int i=0;i<20;i++){
                inviteStatic("ucsm",i+"");
            }
        }
    }

    public static void inviteStatic(String inviteCode,String x){

        //模拟h5 推广
        String phone = GenerateCodeUtil.generatePhone();
        System.out.println(phone);
        Long timestamp = System.currentTimeMillis();
        String salt = "abcdefg123456";
        String url = "http://localhost:8080/loanStream/api/sendPromotionPhoneCode?" +
                "phone="+ phone+"&type=8&" +
                "inviteCode="+inviteCode+"&" +
                "x="+x+"&timestamp="+timestamp+"&" +
                "sign="+ MD5Util.md5(salt + timestamp);
        String response = HttpClientTool.get(url);
        System.out.println(response);
        DBConnect.setDbEnum(DBEnum.LOANSTREAM_HOME);
        String sql = "select * from t_app_user_sms where phone = '"+phone+"' ORDER BY id desc limit 1";
        Map<String,String> map = (Map<String,String>)QuerySQL.me().getOneColumn(true).queryBySql(sql);
        String code = map.get("send_code");

        System.out.println(code);
        timestamp = System.currentTimeMillis();
        //模拟注册登录
        url = "http://localhost:8080/loanStream/api/login?" +
                "validcode="+code+"&phone="+phone+"&" +
                "timestamp="+timestamp+"&" +
                "sign=" + MD5Util.md5(salt + timestamp);
        response = HttpClientTool.get(url);
        System.out.println(response);
    }
}
