package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

//Multipart类会读取request会使得后续io读取不了
@SpringBootApplication(scanBasePackages = {"com.example"},exclude = {MultipartAutoConfiguration.class})
public class LocalFileRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalFileRepositoryApplication.class, args);


    }


}
