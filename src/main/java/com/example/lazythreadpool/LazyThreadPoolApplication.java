package com.example.lazythreadpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Future;

@SpringBootApplication
public class LazyThreadPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyThreadPoolApplication.class, args);
        Future<?> re = LazyThreadPool.getExecutorService().submit(new TestTask());
    }
}
