package com.taolc.http.vo;


import com.taolc.http.enums.ResponseEnum;

/**
 * 返回响应内容
 */
public class ResponseData {

    private String code;
    private String message;
    private Object data;

    public ResponseData(ResponseEnum responseEnum){
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseData(ResponseEnum responseEnum, Object data){
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public ResponseData(String code, String message){
        this.code = code;
        this.message = message;
    }

    public ResponseData(String code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
