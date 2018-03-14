package com.mmall.beans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/14
 * To change this template use File | Settings | File Templates.
 */

public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo=1;

    @Min(value = 1,message = "每页展示必须为1")
    @Getter
    @Setter
    private int pageSize=10;

    @Setter
    private int offset;

    public int getOffset(){
        return (pageNo-1)*pageSize;


    }
}
