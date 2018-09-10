package com.taolc.http.youka_openApi;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.HttpClientTool;
import com.taolc.http.util.GenerateCodeUtil;
import com.taolc.http.util.JWTUtil;
import com.taolc.util.DateUtil;
import tdh.platform.utruck.openapi.DC;
import tdh.platform.utruck.openapi.entity.ApiRequest;
import tdh.platform.utruck.openapi.entity.ApiSubject;
import tdh.platform.utruck.openapi.service.common.order.entity.request.*;
import tdh.platform.utruck.openapi.service.common.pay.entity.request.UtOrderDetailRequest;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtDriverRequest;
import tdh.platform.utruck.openapi.service.common.user.entity.request.CrtShipperRequset;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 优卡 openApi测试
 */
public class OpenApiTest {
    public static void main(String[] args) {
        OpenApiTest me = me();
        me.flowA();
    }

    /**
     * A 流程 注册货主 - 注册司机 - 创建订单（自动接单）  - 装车 - 发车 - 到达 - 卸货 - 签收 - 订单查询
     */
    private void flowA(){
        String shipper = GenerateCodeUtil.generatePhone();
        String driver = GenerateCodeUtil.generatePhone();
        String orderNo = "DD" + System.currentTimeMillis();
        registerShipper(shipper);
        registerDriver(driver);
        create(shipper,driver,orderNo);
        loadTruck(driver,orderNo);
//        startTruck();
//        arriveTruck();
//        unloadTruck();
//        receiving();
//        orderDetail();
    }

    /**
     * B 流程 注册货主- 注册司机 - 创建订单 - 指派司机 - 装车 - 发车 - 到达 - 卸货 - 确认到达 - 签收 - 订单查询
     */
    private void flowB(){
    }

    public static OpenApiTest me(){
        return new OpenApiTest();
    }

    /**
     * 指派司机
     */
    private void assignDriverOrder(String shipperMobile,String driverMobile,String orderNo){
        UtAssignOrderReqVo utAssignOrderReqVo = new UtAssignOrderReqVo();
        utAssignOrderReqVo.setSourceOrderNo(orderNo);
        utAssignOrderReqVo.setCarrierMobile(driverMobile);
        utAssignOrderReqVo.setCharge(new BigDecimal("1111.11"));
        basePostJson(utAssignOrderReqVo,"utOrderOpenService/assignDriverOrder",shipperMobile);
    }

    /**
     * 创建优卡订单(不包含司机信息)
     */
    private void createNoDriver(String shipperMobile,String orderNo){
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
        utruckOrderBody.setSourceOrderNo(orderNo);
        basePostJson(utruckOrderBody,"utOrderOpenService/create",shipperMobile);
    }

    /**
     * 创建优卡订单 (包含司机)
     */
    public void create(String shipperMobile,String driverMobile,String orderNo){
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
        utruckOrderBody.setExpectArriveTime(DateUtil.addDays(1));
        utruckOrderBody.setSourceOrderNo(orderNo);
        utruckOrderBody.setCarrierTypeCd(DC.UtCarrierType.S);
        utruckOrderBody.setCarrierMobile(driverMobile);
        utruckOrderBody.setDriverMobile(driverMobile);
        utruckOrderBody.setTotalCharge(new BigDecimal(1111));
        basePostJson(utruckOrderBody,"utOrderOpenService/create",shipperMobile);
    }

    /**
     * 订单查询
     */
    public void orderDetail(String shipperMobile,String orderNo){
        UtOrderDetailRequest utOrderDetailRequest = new UtOrderDetailRequest();
        utOrderDetailRequest.setSourceOrderNo(orderNo);
        basePostJson(utOrderDetailRequest,"utOrderOpenService/detail",shipperMobile);
    }

    /**
     * 装车测试
     */
    public void loadTruck(String driverMobile,String orderNo){
        basePostJson(getUtPickOrderReqVo(orderNo),"utOrderOpenService/loadTruck",driverMobile);
    }

    private UtPickOrderReqVo getUtPickOrderReqVo(String orderNo){
        UtPickOrderReqVo utPickOrderReqVo = new UtPickOrderReqVo();
        utPickOrderReqVo.setRemark("1111");
        utPickOrderReqVo.setSourceOrderNo(orderNo);
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
    public void startTruck(String driverMobile,String orderNo){
        basePostJson(getUtPickOrderReqVo(orderNo),"utOrderOpenService/startTruck",driverMobile);
    }

    /**
     * 到达测试
     */
    public void arriveTruck(String driverMobile,String orderNo){
        basePostJson(getUtPickOrderReqVo(orderNo),"utOrderOpenService/arriveTruck",driverMobile);
    }

    /**
     * 卸货测试
     */
    public void unloadTruck(String driverMobile,String orderNo){
        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        basePostJson(getUtPickOrderReqVo(orderNo),"utOrderOpenService/unloadTruck",driverMobile);
    }

    /**
     * 签收测试
     */
    public void receiving(String driverMobile,String orderNo){
        basePostJson(getUtPickOrderReqVo(orderNo),"utOrderOpenService/receiving",driverMobile);
    }

    /**
     * 注册货主
     */
    public void registerShipper(String mobile){
        CrtShipperRequset crtShipperRequset = new CrtShipperRequset();
        crtShipperRequset.setMobile(mobile);
        crtShipperRequset.setCompanyName("百及");
        crtShipperRequset.setFixedTel("");
        crtShipperRequset.setUsername("taolc");

        List<UtOrderFileInfo> utOrderFileInfos = new ArrayList<>();
        UtOrderFileInfo utOrderFileInfo = new UtOrderFileInfo();
        utOrderFileInfo.setFileName("111");
        utOrderFileInfo.setUrl("http://taolc.vicp.cc/images/20180829/original/03494af8c9b541e8b5240aca09621e55.jpg");
        utOrderFileInfo.setRefType(DC.FileType.P10);
        utOrderFileInfos.add(utOrderFileInfo);
        crtShipperRequset.setImages(utOrderFileInfos);
        basePostJson(crtShipperRequset,"utUserOpenService/registerShipper",null);
    }

    /**
     * 注册司机
     */
    public void registerDriver(String mobile){
        CrtDriverRequest crtDriverRequest = new CrtDriverRequest();
        crtDriverRequest.setMobile(mobile);
        crtDriverRequest.setUsername("taolc");
        crtDriverRequest.setVehicleLength("12");
        crtDriverRequest.setVehicleModel("10");
        crtDriverRequest.setVehiclePlateNo("京A11111");
        crtDriverRequest.setVehicleLoad(20);

        List<UtOrderFileInfo> utOrderFileInfos = new ArrayList<>();
        UtOrderFileInfo utOrderFileInfo = new UtOrderFileInfo();
        utOrderFileInfo.setFileName("2222");
        utOrderFileInfo.setUrl("http://taolc.vicp.cc/images/20180829/original/03494af8c9b541e8b5240aca09621e55.jpg");
        utOrderFileInfo.setRefType(DC.FileType.P60);
        utOrderFileInfos.add(utOrderFileInfo);
        crtDriverRequest.setImages(utOrderFileInfos);
        basePostJson(crtDriverRequest,"utUserOpenService/registerDriver",null);
    }

    /**
     * post json 基础调用
     * @param t
     * @param methodName
     */
    private <T> void basePostJson(T t,String methodName,String mobile){
        ApiRequest<T> request = new ApiRequest<>();
        request.setBody(t);
        request.setOperaTime(new Date());
        ApiSubject apiSubject = new ApiSubject();
        apiSubject.setUserMobile(mobile);
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
