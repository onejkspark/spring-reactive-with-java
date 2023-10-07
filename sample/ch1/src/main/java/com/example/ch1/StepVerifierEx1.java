package com.example.ch1;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class StepVerifierEx1 {

    public Flux<Integer> getInts() {
        return Flux.range(1, 100);
    }

    public Flux<String> getStrings() {
        return Flux.fromIterable(List.of("foo", "bar"));
    }

    public Flux<Integer> getError() {
        return Flux.just(1, 2).concatWith(
                Mono.error(new IllegalArgumentException(getMsg()))
        );
    }

    protected String getMsg() {
        return "Our message";
    }
}
