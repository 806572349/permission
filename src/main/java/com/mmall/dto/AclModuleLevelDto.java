package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/15
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class AclModuleLevelDto extends SysAclModule {
    List<AclModuleLevelDto> aclModuleList= Lists.newArrayList();

    public static  AclModuleLevelDto adapt(SysAclModule sysAclModule){
        AclModuleLevelDto aclModuleLevelDto = new AclModuleLevelDto();
        BeanUtils.copyProperties(sysAclModule,aclModuleLevelDto);
        return  aclModuleLevelDto;

    }
}
