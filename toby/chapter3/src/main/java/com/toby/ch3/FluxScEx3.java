package com.toby.ch3;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx3 {

    public static void main(String[] args) throws InterruptedException {


        Flux.interval(Duration.ofMillis(200))
                .take(10)
                .subscribe(s ->
                        log.info("onNext : {}", s)
                );

        log.info("exit");
        TimeUnit.SECONDS.sleep(10);

    }
}
