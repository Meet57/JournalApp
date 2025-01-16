package com.meet.myFirstProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.meet.myFirstProject")
public class JournalApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JournalApplication.class, args);
    }
}