package com.toby.ch4.app3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@Slf4j
@EnableAsync
@SpringBootApplication
public class App3Application {

    public static void main(String[] args) {
        SpringApplication.run(App3Application.class, args);
    }

    @RestController
    public static class MyController {

        @GetMapping("/async")
        public Callable<String> async() throws InterruptedException {
            log.info("callback");
            return () -> {
                log.info("async");
                Thread.sleep(2000);
                return "hello";
            };
        }

        @GetMapping("/async2")
        public String async2() throws InterruptedException {
            log.info("callback");
            Thread.sleep(2000);
            return "hello";
        }
    }

    @Component
    public static class MyService {
        @Async
        public ListenableFuture<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }
    }

}
