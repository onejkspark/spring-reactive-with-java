package com.toby.ch4.app5;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;

@Slf4j
@EnableAsync
@SpringBootApplication
public class App5Application {

    public static void main(String[] args) {
        SpringApplication.run(App5Application.class, args);
    }

    @RestController
    public static class MyController {

        @GetMapping("/emitter")
        public ResponseBodyEmitter emitter() throws InterruptedException {

            ResponseBodyEmitter em = new ResponseBodyEmitter();

            Executors.newCachedThreadPool().submit(() -> {

                for (int i = 0; i < 50; i++) {
                    try {
                        em.send("{\"index\":" + i + "}");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            });

            return em;
        }

    }

}
