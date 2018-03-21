package com.mmall.util;

import com.mmall.beans.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class MailUtil {

    public static boolean send(Mail mail) {

        // TODO ifnvdukbfoesbegh
        String from = "806572349@qq.com";
        int port = 587;
        String host = "smtp.qq.com";
        String pass = "ifnvdukbfoesbegh";
        String nickname = "老司机后台管理";

        HtmlEmail email = new HtmlEmail();
        try {
            email.setHostName(host);
            email.setCharset("UTF-8");
            for (String str : mail.getReceivers()) {
                email.addTo(str);
            }
            email.setFrom(from, nickname);
            email.setSmtpPort(port);
            email.setAuthentication(from, pass);
            email.setSubject(mail.getSubject());
            email.setMsg(mail.getMessage());
            email.send();
            log.info("{} 发送邮件到 {}", from, StringUtils.join(mail.getReceivers(), ","));
            return true;
        } catch (EmailException e) {
            log.error(from + "发送邮件到" + StringUtils.join(mail.getReceivers(), ",") + "失败", e);
            return false;
        }
    }

    public static  void sendAsyc(Mail mail){
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<?> submit = es.submit(() -> {
            send(mail);
        });
    }


//     public static  void main(String[] args){
////         522310157@qq.com
//         Set<String> set=new HashSet<>();
//         set.add("522310157@qq.com");
//         Mail mail = Mail.builder().message("test").receivers(set).subject("test").build();
//         MailUtil.send(mail);
//
//
//     }
}

