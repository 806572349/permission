package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
public class LevleUtil {

    public  final static String SEPARATOR=".";


    public final static  String ROOT="0";
    //0
    //0.1
    //0.1.2
    public static  String calculateLevel(String parentLevel,int parentId){
            if (StringUtils.isBlank(parentLevel)){
                return ROOT;
            }else {
                return StringUtils.join(parentLevel,SEPARATOR,parentId);
            }

    }

}
