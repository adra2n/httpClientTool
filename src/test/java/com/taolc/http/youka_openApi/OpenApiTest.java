package com.taolc.http.youka_openApi;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;
import tdh.platform.utruck.openapi.service.common.order.entity.request.UtOrderCargo;
import tdh.platform.utruck.openapi.service.common.order.entity.request.UtOrderContact;
import tdh.platform.utruck.openapi.service.common.order.entity.request.UtPickOrderReqVo;
import tdh.platform.utruck.openapi.service.common.order.entity.request.UtruckOrderBody;
import tdh.thunder.rpc.common.ApiRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优卡 openApi测试
 */
public class OpenApiTest {

    public static void main(String[] args) {
        OpenApiTest me = me();
        me.loadTruck();
    }

    public static OpenApiTest me(){
        return new OpenApiTest();
    }

    /**
     * 创建优卡订单
     */
    public void create(){
        UtruckOrderBody utruckOrderBody = new UtruckOrderBody();
        List<UtOrderContact> utOrderContacts = new ArrayList<>();
        UtOrderCargo utOrderCargo = new UtOrderCargo();
        utruckOrderBody.setContactList(utOrderContacts);
        utruckOrderBody.setCargo(utOrderCargo);
        utruckOrderBody.setExpectDeliverTime(new Date());
        utruckOrderBody.setExpectArriveTime(new Date());

    }

    /**
     * 装车测试
     */
    public void loadTruck(){
        UtPickOrderReqVo utPickOrderReqVo = new UtPickOrderReqVo();
        utPickOrderReqVo.setRemark("1111");
        utPickOrderReqVo.setSourceOrderNo("");
        basePostJson(utPickOrderReqVo,"utOrderOpenService/loadTruck");
    }

    /**
     * post json 基础调用
     * @param t
     * @param methodName
     */
    private <T> void basePostJson(T t,String methodName){
        ApiRequest<T> request = new ApiRequest<>();
        request.setBody(t);
        String response = HttpClientTool.postJson(getUrl(methodName),JSONArray.toJSONString(request));
        System.out.println(response);
    }

    private String getUrl(String methodName){
        return "http://localhost:9991/" + methodName;
    }
}
