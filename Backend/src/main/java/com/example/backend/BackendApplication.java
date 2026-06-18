package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println("Use este valor para a password 123->" + encode);
        //System.out.println(bCryptPasswordEncoder.matches("1234",encode));
        //System.out.println(bCryptPasswordEncoder.matches("123",encode));

        SpringApplication.run(BackendApplication.class, args);

    }

}
