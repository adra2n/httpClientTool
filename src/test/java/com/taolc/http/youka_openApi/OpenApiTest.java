package com.taolc.http.youka_openApi;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;
import com.taolc.http.util.GenerateCodeUtil;
import tdh.platform.utruck.openapi.entity.ApiRequest;
import tdh.platform.utruck.openapi.entity.ApiSubject;
import tdh.platform.utruck.openapi.service.common.order.entity.request.*;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtDriverRequest;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtShipperRequset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优卡 openApi测试
 */
public class OpenApiTest {

    public static void main(String[] args) {
        OpenApiTest me = me();
//        me.registerDriver();
        me.registerShipper();
//        me.loadTruck();
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
     * 注册货主
     */
    public void registerShipper(){
        CrtShipperRequset crtShipperRequset = new CrtShipperRequset();
        crtShipperRequset.setMobile(GenerateCodeUtil.generatePhone());
        crtShipperRequset.setCompanyName("百及");
        crtShipperRequset.setFixedTel("");
        crtShipperRequset.setUsername("taolc");

        List<UtOrderFileInfo> utOrderFileInfos = new ArrayList<>();
        UtOrderFileInfo utOrderFileInfo = new UtOrderFileInfo();
        utOrderFileInfo.setFileName("111");
        utOrderFileInfo.setUrl("2222");
        utOrderFileInfos.add(utOrderFileInfo);
        crtShipperRequset.setImages(utOrderFileInfos);
        basePostJson(crtShipperRequset,"utUserOpenService/registerShipper");
    }

    /**
     * 注册司机
     */
    public void registerDriver(){
        CrtDriverRequest crtDriverRequest = new CrtDriverRequest();
        crtDriverRequest.setMobile("13681975404");
        crtDriverRequest.setUsername("taolc");
        crtDriverRequest.setVehicleLength("12.22");
        crtDriverRequest.setVehicleModel("s1122");
        crtDriverRequest.setVehiclePlateNo("沪C·66666");
        crtDriverRequest.setVehicleLoad(20);
        basePostJson(crtDriverRequest,"utUserOpenService/registerDriver");
    }

    /**
     * post json 基础调用
     * @param t
     * @param methodName
     */
    private <T> void basePostJson(T t,String methodName){
        ApiRequest<T> request = new ApiRequest<>();
        request.setBody(t);
        request.setOperaTime(new Date());
        ApiSubject apiSubject = new ApiSubject();
        apiSubject.setUserMobile("13681975404");
        request.setSubject(apiSubject);
        System.out.println("请求参数 --> " + JSONArray.toJSONString(request));
        String response = HttpClientTool.postJson(getUrl(methodName),JSONArray.toJSONString(request));
        System.out.println(response);
    }

    private String getUrl(String methodName){
        return "http://localhost:9991/" + methodName;
    }
}
