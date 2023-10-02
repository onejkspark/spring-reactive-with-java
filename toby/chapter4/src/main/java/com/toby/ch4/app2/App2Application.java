package com.toby.ch4.app2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@EnableAsync
@SpringBootApplication
public class App2Application {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext c = SpringApplication.run(App2Application.class, args)) {
        }
    }

    @Autowired
    MyService myService;

    @Bean
    ApplicationRunner runner() {
        return args -> {
            log.info("run()");

            //hello()
            ListenableFuture<String> f = myService.hello();
            f.addCallback(s -> log.info("Callback : {}", s), e -> log.info("onError : {}", e.getMessage()));

            //hello()
            ListenableFuture<String> f2 = myService.hello2();
            f2.addCallback(s -> log.info("Callback : {}", s), e -> log.info("onError : {}", e.getMessage()));

            log.info("exit");
        };
    }

    @Component
    public static class MyService {

        @Async(value = "tp")
        public ListenableFuture<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }

        @Async(value = "tp2")
        public ListenableFuture<String> hello2() throws InterruptedException {
            log.info("hello2()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }
    }

    @Bean
    ThreadPoolTaskExecutor tp() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10);
        te.setMaxPoolSize(100);
        te.setQueueCapacity(200);
        te.setThreadNamePrefix("mythread-");
        te.initialize();
        return te;
    }

    @Bean
    ThreadPoolTaskExecutor tp2() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10);
        te.setMaxPoolSize(100);
        te.setQueueCapacity(200);
        te.setThreadNamePrefix("mythread2-");
        te.initialize();
        return te;
    }
}
