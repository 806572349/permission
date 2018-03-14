package com.mmall.beans;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/14
 * To change this template use File | Settings | File Templates.
 */

@ToString
@Builder
@Data
public class PageResult<T> {
    private List<T> data=Lists.newArrayList();
    private int total=0;
}
