package com.changgou.test;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Base64;

/**
 * @author ZJ
 */
public class TestBCrypt {

    public static void main(String[] args) {
        //获取盐值
        String gensalt = BCrypt.gensalt();
        //密码加盐
        String hashpw = BCrypt.hashpw("123456", gensalt);
        //
        System.out.println("===" + hashpw);

        boolean isTrue = BCrypt.checkpw("123456", "$2a$10$n75t1y8Y2GUc2fSZpDaOuXWA6zHpeejxEcHDyrTSkBmSbjwCyre6");
        System.out.println("====" + isTrue);
    }
}
