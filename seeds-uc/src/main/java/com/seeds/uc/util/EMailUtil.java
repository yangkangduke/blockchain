package com.seeds.uc.util;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
public class EMailUtil {

    public static void send(String sendTo, String code) {
        Email email = new HtmlEmail();
        try {
            email.setHostName("smtp.qq.com");
            email.setCharset("UTF-8");
            email.addTo(sendTo);
            email.setFrom("819628513@qq.com", "Seeds");
            email.setAuthentication("819628513@qq.com", "notypapwvrocbfhb");
            email.setSubject("Registration verification code");
            email.setMsg("Registration verification code：" + code);
            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EMailUtil.send("819628513@qq.com","123456");
    }
}

