package com.example.commonutil.util.request;

import lombok.Data;

@Data
public class R {

    private Integer code;
    private String msg;
    private Object data;

    public R(){

    }

    public static R ok(){
        R r = new R();
        r.setCode(0);
        r.setMsg("success");
        return r;
    }

    public static R ok(Object data){
        R r = new R();
        r.setCode(0);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static R error(){
        R r = new R();
        r.setCode(-1);
        r.setMsg("error");
        return r;
    }

    public static R error(Integer code,String errMsg){
        R r = new R();
        r.setCode(code);
        r.setMsg(errMsg);
        return r;
    }
}
