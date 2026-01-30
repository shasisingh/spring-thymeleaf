package com.nightcrawler.spring.hostel;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringHostelApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(SpringHostelApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(false)
                .registerShutdownHook(true)
                .run(args);
    }
}
