package com.taolc.http.youka_openApi;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;
import tdh.platform.utruck.openapi.service.common.order.entity.request.UtPickOrderReqVo;
import tdh.thunder.rpc.common.ApiRequest;

/**
 * 优卡 openApi测试
 */
public class OpenApiTest {

    public static void main(String[] args) {
        OpenApiTest me = me();
        me.loadTruckTest();
    }

    public static OpenApiTest me(){
        return new OpenApiTest();
    }

    /**
     * 装车测试
     */
    public void loadTruckTest(){
        ApiRequest<UtPickOrderReqVo> request = new ApiRequest<>();
        UtPickOrderReqVo utPickOrderReqVo = new UtPickOrderReqVo();
        utPickOrderReqVo.setRemark("1111");
        utPickOrderReqVo.setSourceOrderNo("22222");
        request.setBody(utPickOrderReqVo);

        String response = HttpClientTool.postJson(getUrl("utOrderOpenService/loadTruck"),JSONArray.toJSONString(request));
        System.out.println(response);
    }

    private String getUrl(String methodName){
        return "http://localhost:9991/" + methodName;
    }
}
