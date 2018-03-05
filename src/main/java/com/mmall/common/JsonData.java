package com.mmall.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class JsonData {

    private boolean ret;

    private String msg;

    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }
    public  static JsonData success(Object data,String msg){
        JsonData js=new JsonData(true);
        js.data=data;
        js.msg=msg;
        return  js;
    }
    public  static JsonData success(Object data){
        JsonData js=new JsonData(true);
        js.data=data;
        return  js;
    }

    public  static JsonData success(){
        return  new JsonData(true);
    }

    public  static JsonData fail(String msg){
        JsonData js=new JsonData(false);
        js.msg=msg;
        return  js;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
