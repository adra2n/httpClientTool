package com.taolc.http.enums;

/**
 * 返回响应码
 */
public enum ResponseEnum {

    OK("0000","响应成功"),
    FAIL("9999","响应失败"),
    PROCESS("300T","处理中"),
    NETWORK_EXCEPTION("400T","网络异常");

    private String code;
    private String message;

    ResponseEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseEnum getByValue(String message){
        for(ResponseEnum temp:ResponseEnum.values()){
            if(temp.getMessage().equals(message)){
                return temp;
            }
        }
        return null;
    }
}
