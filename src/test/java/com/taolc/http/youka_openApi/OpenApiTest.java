package com.taolc.http.youka_openApi;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;
import com.taolc.http.util.GenerateCodeUtil;
import com.taolc.http.util.JWTUtil;
import tdh.platform.utruck.openapi.DC;
import tdh.platform.utruck.openapi.entity.ApiRequest;
import tdh.platform.utruck.openapi.entity.ApiSubject;
import tdh.platform.utruck.openapi.service.common.order.entity.request.*;
import tdh.platform.utruck.openapi.service.common.pay.entity.request.UtOrderDetailRequest;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtDriverRequest;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtShipperRequset;

import java.math.BigDecimal;
import java.util.*;

/**
 * 优卡 openApi测试
 */
public class OpenApiTest {

    private String shipper = "18225696106";
    private String driver = "15210770876";
    private boolean isShipper;

    public static void main(String[] args) {
        //两个流程
        // 一种 自动接单 - 装车 - 发车 - 到达 - 卸货 - 签收
        // 二种 指派司机 - 装车 - 发车 - 到达 - 卸货 - 确认到达 - 签收
        OpenApiTest me = me();
//        me.registerShipper();
//        me.registerDriver();
//        me.create();
        me.orderDetail();
//        me.loadTruck();
//         me.startTruck();
//        me.arriveTruck();
//        me.unloadTruck();
//        me.receiving();
    }

    public static OpenApiTest me(){
        return new OpenApiTest();
    }

    /**
     * 创建优卡订单
     */
    public void create(){

        isShipper = true;

        UtruckOrderBody utruckOrderBody = new UtruckOrderBody();

        List<UtOrderContact> utOrderContacts = new ArrayList<>();
        UtOrderContact utOrderContact = new UtOrderContact();
        utOrderContact.setName("张三");
        utOrderContact.setMobile(GenerateCodeUtil.generatePhone());
        utOrderContact.setAddress("上海市");
        utOrderContact.setCity(310000);
        utOrderContact.setContactClass(DC.CargoRole.SEND);
        utOrderContacts.add(utOrderContact);

        utOrderContact = new UtOrderContact();
        utOrderContact.setName("李四");
        utOrderContact.setMobile(GenerateCodeUtil.generatePhone());
        utOrderContact.setAddress("上海市");
        utOrderContact.setCity(310000);
        utOrderContact.setContactClass(DC.CargoRole.RECE);
        utOrderContacts.add(utOrderContact);

        UtOrderCargo utOrderCargo = new UtOrderCargo();
        utOrderCargo.setWeight(100F);
        utOrderCargo.setVolumn(10F);
        utOrderCargo.setTruckType(DC.UtTruckType.DL);
        utOrderCargo.setTruckLength(DC.UtTruckSize.L420);
        utOrderCargo.setCargoName("机器人");
        utOrderCargo.setCargoTypeCd(DC.UtCargoType.DZCP);


        utruckOrderBody.setContactList(utOrderContacts);
        utruckOrderBody.setCargo(utOrderCargo);
        utruckOrderBody.setExpectDeliverTime(new Date());
        utruckOrderBody.setExpectArriveTime(new Date());
        utruckOrderBody.setSourceOrderNo("DD002");
        utruckOrderBody.setCarrierTypeCd(DC.UtCarrierType.S);
        utruckOrderBody.setCarrierMobile(driver);
        utruckOrderBody.setTotalCharge(new BigDecimal(1111.11));
        basePostJson(utruckOrderBody,"utOrderOpenService/create");
    }

    /**
     * 订单查询
     */
    public void orderDetail(){
        isShipper = true;

        UtOrderDetailRequest utOrderDetailRequest = new UtOrderDetailRequest();
        utOrderDetailRequest.setSourceOrderNo("DD001");
        basePostJson(utOrderDetailRequest,"utOrderOpenService/detail");
    }

    /**
     * 装车测试
     */
    public void loadTruck(){
        basePostJson(getUtPickOrderReqVo(),"utOrderOpenService/loadTruck");
    }

    private UtPickOrderReqVo getUtPickOrderReqVo(){
        isShipper = false;
        UtPickOrderReqVo utPickOrderReqVo = new UtPickOrderReqVo();
        utPickOrderReqVo.setRemark("1111");
        utPickOrderReqVo.setSourceOrderNo("DD001");
        List<UtOrderFileInfo> utOrderFileInfos = new ArrayList<>();
        UtOrderFileInfo utOrderFileInfo = new UtOrderFileInfo();
        utOrderFileInfo.setFileName("111");
        utOrderFileInfo.setUrl("2222");
        utPickOrderReqVo.setImages(utOrderFileInfos);
        return utPickOrderReqVo;
    }

    /**
     * 发车测试
     */
    public void startTruck(){
        basePostJson(getUtPickOrderReqVo(),"utOrderOpenService/startTruck");
    }

    /**
     * 到达测试
     */
    public void arriveTruck(){
        basePostJson(getUtPickOrderReqVo(),"utOrderOpenService/arriveTruck");
    }

    /**
     * 卸货测试
     */
    public void unloadTruck(){
        basePostJson(getUtPickOrderReqVo(),"utOrderOpenService/unloadTruck");
    }

    /**
     * 签收测试
     */
    public void receiving(){
        basePostJson(getUtPickOrderReqVo(),"utOrderOpenService/receiving");
    }

    /**
     * 注册货主
     */
    public void registerShipper(){
        isShipper = true;
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
        isShipper = true;
        CrtDriverRequest crtDriverRequest = new CrtDriverRequest();
        crtDriverRequest.setMobile(GenerateCodeUtil.generatePhone());
        crtDriverRequest.setUsername("taolc");
        crtDriverRequest.setVehicleLength("12.22");
        crtDriverRequest.setVehicleModel("s1122");
        crtDriverRequest.setVehiclePlateNo("沪C66666");
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
        if(isShipper){
            apiSubject.setUserMobile(shipper);
        }else{
            apiSubject.setUserMobile(driver);
        }
        request.setSubject(apiSubject);
        System.out.println("请求参数 --> " + JSONArray.toJSONString(request));

        Map<String,String> headers = new HashMap<>();
        String jwtKey = JWTUtil.createToken("3e61e1f7003f4c42a0030d58eeb0d036", "32fddbd4f3e34e3aad1cac72c933cdf7");
        String headerValue ="Bearer "+ jwtKey;
        headers.put("Authorization",headerValue);
        String response = HttpClientTool.postJson(getUrl(methodName),headers,JSONArray.toJSONString(request));
        System.out.println(response);
    }

    private String getUrl(String methodName){
        return "http://192.168.72.82:8000/" + methodName;
    }
}
