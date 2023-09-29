package com.toby.ch2;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class PubSub {

    public static void main(String[] args) {

        // Publisher 선언
        Publisher<Integer> pub = new Publisher<Integer>() {

            final Iterable<Integer> iter = Stream.iterate(1, a -> a + 1).limit(10)
                    .collect(Collectors.toList());

            @Override
            public void subscribe(Subscriber<? super Integer> sub) {

                sub.onSubscribe(
                        new Subscription() {
                            @Override
                            public void request(long n) {
                                try {
                                    iter.forEach(i -> sub.onNext(i));
                                    // 완료
                                    sub.onComplete();
                                } catch (Exception e) {
                                    // 예외
                                    sub.onError(e);
                                }
                            }

                            @Override
                            // 장시간의 구독 취소를 위한 ... !!
                            public void cancel() {

                            }
                        }
                );

            }
        };

        // Subscriber 선언
        Subscriber<Integer> sub = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                // 무제한 데이터 발생
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("onNext : {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        pub.subscribe(sub);

    }
}
