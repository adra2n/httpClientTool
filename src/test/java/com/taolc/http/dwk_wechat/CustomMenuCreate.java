package com.taolc.http.dwk_wechat;

import com.alibaba.fastjson.JSONObject;
import com.taolc.http.HttpClientTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class CustomMenuCreate {

    private static final Logger logger = LoggerFactory.getLogger(CustomMenuCreate.class);

    public static void main(String[] args) {
        qqMemu();
//        wechatMemu();
    }

    /**
     * QQ 自定义菜单
     */
    public static void qqMemu(){
        String qqAccessTokenUrl = "http://test.qquser.mur.qq.com/adminext/getAccessToken.do";
        String qq_access_token = HttpClientTool.post(qqAccessTokenUrl);
        JSONObject jsonObject = JSONObject.parseObject(qq_access_token);
        String access_token = (String) jsonObject.get("json");
        logger.info("获取到qq的accessToken为 --> {}",access_token);
        if(StringUtils.isEmpty(access_token)){
            return;
        }

        String deleteUrl = "https://api.mp.qq.com/cgi-bin/menu/delete?access_token="+access_token;
        String response = HttpClientTool.get(deleteUrl);
        logger.info("删除自定义菜单 --> {}",response);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String createUrl = "https://api.mp.qq.com/cgi-bin/menu/create?access_token="+access_token;
        String json = "{\"button\":[{\"name\":\"走近游戏\",\"sub_button\":[" +
                "{\"name\":\"新品体验\",\"type\":\"view\",\"url\":\"http:\\/\\/api.qquser.mur.qq.com\\/App\\/www\\/#\\/home\"}" +
                ",{\"name\":\"兴趣交流\",\"type\":\"view\",\"url\":\"http:\\/\\/buluo.qq.com\\/mobile\\/barindex.html?_bid=128&_wv=1027&bid=342763\"}" +
                "]},{\"name\":\"参与研究\",\"type\":\"view\",\"url\":\"https:\\/\\/open.mp.qq.com\\/connect\\/oauth2\\/authorize?appid=200912033&redirect_uri=http%3a%2f%2fapi.qquser.mur.qq.com%2fApp%2fwww%2findex.html%23%2ftransition&response_type=code&scope=snsapi_base&state=STATE#qq_redirect\"}" +
                ",{\"name\":\"领取奖励\",\"sub_button\":[{\"name\":\"玩咖认证\",\"type\":\"view\",\"url\":\"http:\\/\\/api.qquser.mur.qq.com\\/App\\/www\\/#\\/user\\/cert\"}" +
                ",{\"name\":\"奖品兑换\",\"type\":\"view\",\"url\":\"http:\\/\\/api.qquser.mur.qq.com\\/App\\/www\\/#\\/productList\"}" +
                ",{\"name\":\"推荐好友\",\"type\":\"view\",\"url\":\"http:\\/\\/api.qquser.mur.qq.com\\/App\\/www\\/#\\/invertFriends\"}" +
                ",{\"name\":\"个人中心\",\"type\":\"view\",\"url\":\"http:\\/\\/api.qquser.mur.qq.com\\/App\\/www\\/#\\/me\"}]}]}";
        response = HttpClientTool.postJson(createUrl,json);
        logger.info("创建自定义菜单 --> {}",response);

        String getUrl = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+access_token;
        String getResponse = HttpClientTool.get(getUrl);
        logger.info("获取自定义菜单的json数据 --> {}",getResponse);
    }

    /**
     * QQ 自定义菜单
     */
    public static void wechatMemu(){
        String qqAccessTokenUrl = "http://test.qquser.mur.qq.com/adminext/getAccessToken.do";
        String qq_access_token = HttpClientTool.post(qqAccessTokenUrl);
        JSONObject jsonObject = JSONObject.parseObject(qq_access_token);
        String access_token = (String) jsonObject.get("json");
        logger.info("获取到wechat的accessToken为 --> {}",access_token);
        if(StringUtils.isEmpty(access_token)){
            return;
        }

        String deleteUrl = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+access_token;
        String response = HttpClientTool.get(deleteUrl);
        logger.info("删除自定义菜单 --> {}",response);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String createUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
        String json = "{\"button\":[{\"name\":\"走近游戏\",\"sub_button\":[" +
                "{\"name\":\"新品体验\",\"type\":\"view\",\"url\":\"http:\\/\\/api.user.mur.qq.com\\/App\\/www\\/#\\/home\"}" +
                ",{\"name\":\"兴趣交流\",\"type\":\"view\",\"url\":\"http:\\/\\/buluo.qq.com\\/mobile\\/barindex.html?_bid=128&_wv=1027&bid=342763\"}" +
                ",{\"name\":\"游戏资讯\",\"type\":\"view\",\"url\":\"https://mp.weixin.qq.com/mp/homepage?__biz=MzAwMjc0NDM1Mw%3D%3D&hid=1&sn=2f296b19e16630b1d3af258c6c1394fb\"}" +
                "]},{\"name\":\"参与研究\",\"type\":\"view\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wxc856ee57e4f81136&redirect_uri=http%3a%2f%2fapi.user.mur.qq.com%2fApp%2fwww%2findex.html%23%2ftransition&response_type=code&scope=snsapi_base&state=STATE#qq_redirect\"}" +
                ",{\"name\":\"领取奖励\",\"sub_button\":[{\"name\":\"玩咖认证\",\"type\":\"view\",\"url\":\"http:\\/\\/api.user.mur.qq.com\\/App\\/www\\/#\\/user\\/cert\"}" +
                ",{\"name\":\"奖品兑换\",\"type\":\"view\",\"url\":\"http:\\/\\/api.user.mur.qq.com\\/App\\/www\\/#\\/productList\"}" +
                ",{\"name\":\"推荐好友\",\"type\":\"view\",\"url\":\"http:\\/\\/api.user.mur.qq.com\\/App\\/www\\/#\\/invertFriends\"}" +
                ",{\"name\":\"个人中心\",\"type\":\"view\",\"url\":\"http:\\/\\/api.user.mur.qq.com\\/App\\/www\\/#\\/me\"}]}]}";
        response = HttpClientTool.postJson(createUrl,json);
        logger.info("创建自定义菜单 --> {}",response);

        String getUrl = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+access_token;
        String getResponse = HttpClientTool.get(getUrl);
        logger.info("获取自定义菜单的json数据 --> {}",getResponse);
    }
}
