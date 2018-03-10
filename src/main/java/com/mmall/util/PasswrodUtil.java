package com.mmall.util;

import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
public class PasswrodUtil {
    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };
    public  static String randomPasswrod(){
        StringBuffer stringBuffer=new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag=false;
        int lenth = random.nextInt(3) + 8;
        for (int i=0;i<lenth;i++){
            if (flag){
                stringBuffer.append(num[random.nextInt(num.length)]);
            }else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag=!flag;
        }
        return stringBuffer.toString();

    }

 public static  void main(String[] args){
     System.out.println(randomPasswrod());

     }

}
