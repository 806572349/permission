package com.mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/15
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String subject;

    private String message;

    private Set<String> receivers;
}
