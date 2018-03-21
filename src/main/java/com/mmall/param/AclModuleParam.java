package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/15
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@ToString
public class AclModuleParam {
    private Integer id;

    @NotBlank(message = "权限模块不能以为空")
    @Length(min = 2,max = 20,message = "权限模块名称长度2-64")
    private String name;


    private Integer parentId=0;

    @NotNull(message = "权限模块展示顺序为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Min(value = 0,message = "权限状态不合法")
    @Max(value = 1,message = "权限状态不合法")
    private Integer status;

    @Length(max = 200,message = "权限模块备注长度200之间")
    private String remark;
}
