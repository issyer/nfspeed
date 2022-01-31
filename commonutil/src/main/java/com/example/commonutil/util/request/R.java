package com.example.commonutil.util.request;

import lombok.Data;

@Data
public class R<T> {

    private Integer code;
    private String msg;
    private T data;

    public R(){}

    public R(Integer code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<T>(0, "success", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(0, "success", data);
    }

    public static <T> R<T> error() {
        return new R<T>(-1, "error", null);
    }

    public static <T> R<T> error(Integer code,String msg) {
        return new R<T>(code, msg, null);
    }


}
