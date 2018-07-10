package com.taolc.http.controller;

import com.taolc.http.enums.ResponseEnum;
import com.taolc.http.vo.ResponseData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/http")
public class HttpController {

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ResponseData get(){
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData("this is get http");
        return responseData;
    }

    @ResponseBody
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    public ResponseData post(){
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData("this is post http");
        return responseData;
    }
}
