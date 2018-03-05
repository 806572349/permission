package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class TestVo {
    @NotBlank
    private  String msg;
    @NotNull(message = "不能为空")
    @Max(value = 10,message = "id 不能大于10")
    private  Integer id;
}
