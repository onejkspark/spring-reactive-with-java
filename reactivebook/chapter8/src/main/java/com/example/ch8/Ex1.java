package com.example.ch8;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class Ex1 {

    public static void main(String[] args) {

        Flux.range(1, 5).doOnRequest(d ->

            log.info("# doOnRequest : {}", d)).subscribe(new BaseSubscriber<Integer>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("# hookOnNext : {}", value);
                request(1);
            }
        });
    }
}
