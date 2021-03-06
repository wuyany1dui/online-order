package com.design.onlineorder.exception;

/**
 * @author DrEAmSs
 */
public class MyException extends RuntimeException {

    private Integer code;

    public MyException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
