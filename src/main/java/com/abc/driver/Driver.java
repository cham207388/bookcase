package com.abc.driver;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This is my trial and error class for most of the concepts I am not sure of
 */
public class Driver {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("mypassword"));
    }
}
