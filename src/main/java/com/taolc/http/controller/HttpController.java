package com.taolc.http.controller;

import com.taolc.http.enums.ResponseEnum;
import com.taolc.http.model.User;
import com.taolc.http.vo.ResponseData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/http")
public class HttpController {

    @GetMapping(value = "/get")
    public ResponseData get(User user) {
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData(user);
        return responseData;
    }

    @PostMapping(value = "/post")
    public ResponseData post(User user) {
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData(user);
        return responseData;
    }

    @PostMapping(value = "/postJson")
    public ResponseData postJson(@RequestBody User user) {
        ResponseData responseData = new ResponseData(ResponseEnum.OK);
        responseData.setData(user);
        return responseData;
    }
}
