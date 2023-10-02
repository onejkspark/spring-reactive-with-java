package com.toby.ch4.app1;

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
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Slf4j
@EnableAsync
@SpringBootApplication
public class App1Application {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext c = SpringApplication.run(App1Application.class, args)) {
        }
    }

    @Autowired
    MyService myService;

    @Bean
    ApplicationRunner runner() {
        return args -> {
            log.info("run()");
            Future<String> res = myService.hello();
            log.info("exit : {}", res.isDone());
            log.info("result : {}", res.get());
        };
    }

    @Component
    public static class MyService {

        @Async
        public Future<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(1000);
            return new AsyncResult<>("Hello");
        }
    }
}
