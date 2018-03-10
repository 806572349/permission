package com.mmall.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
@Data
public class UserParm {
    private Integer id;

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1,max = 20,message = "用户名长度1-20")
    private String username;
    @NotBlank(message = "电话不可以为空")
    @Length(min = 1,max = 13,message = "电话名长度1-13")
    private String telephone;

    @NotBlank(message = "邮箱不可以为空")
    @Length(min = 5,max = 50,message = "邮箱需要50长度以内")
    private String mail;
    @NotNull(message = "必须提供用户所在的部门")
    private Integer deptId;
    @NotNull(message = "用户状态")
    @Min(value = 0,message = "用户状态不合法")
    @Max(value = 2,message = "用户状态不合法")
    private Integer status;
    @Length(min = 0,max = 200,message = "备注长度在200以内")
    private String remark;
}
