package com.taolc.http.controller;

import com.alibaba.fastjson.JSONArray;
import com.taolc.http.enums.ResponseEnum;
import com.taolc.http.model.User;
import com.taolc.http.vo.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/http")
public class HttpController {

    @GetMapping(value = "/get")
    public ResponseData get(User user) {
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData(user);
        System.out.println(JSONArray.toJSONString(responseData));
        return responseData;
    }

    @PostMapping(value = "/post")
    public ResponseData post(@RequestBody User user) {
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData(user);
        System.out.println(JSONArray.toJSONString(responseData));
        return responseData;
    }
}
