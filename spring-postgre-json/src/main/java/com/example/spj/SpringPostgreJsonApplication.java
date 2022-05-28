package com.example.spj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringPostgreJsonApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringPostgreJsonApplication.class, args);
    }

    @Autowired
    private AuditService as;

    @Override
    public void run(String... args) throws Exception {

        as.loadData();

        as.copyData();

        as.restoreData();
    }
}
