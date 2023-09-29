package com.toby.ch2;

import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public class ReactorEx3 {

    public static void main(String[] args) {

        Flux.<Integer>fromIterable(
                        () -> Stream.iterate(1, a -> a + 1)
                                .limit(10)
                                .iterator()
                )
                .log()
                .map(m -> m * 100)
                .log()
                .subscribe(System.out::println);

    }
}
