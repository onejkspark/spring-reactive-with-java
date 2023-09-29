package com.toby.ch2;

import reactor.core.publisher.Flux;

public class ReactorEx2 {

    public static void main(String[] args) {
        // Publisher
        Flux.<Integer>create(e -> {
                    e.next(1);
                    e.next(2);
                    e.next(3);
                    e.next(4);
                    e.next(5);
                    e.complete();
                })
                // Operators1
                .log()
                // Operators2
                .map(m -> m * 10)
                // Operators3
                .log()
                .reduce(0, Integer::sum)
                .log()
                // Subscriber
                .subscribe(System.out::println);
    }
}
