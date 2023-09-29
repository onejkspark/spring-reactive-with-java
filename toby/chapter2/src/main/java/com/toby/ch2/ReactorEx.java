package com.toby.ch2;

import reactor.core.publisher.Flux;

public class ReactorEx {

    public static void main(String[] args) {
        // Publisher
        Flux.create(e -> {
                    e.next(1);
                    e.next(2);
                    e.next(3);
                    e.next(4);
                    e.next(5);
                    e.complete();
                })
                // Operators
                .log()
                // Subscriber
                .subscribe(System.out::println);
    }
}
