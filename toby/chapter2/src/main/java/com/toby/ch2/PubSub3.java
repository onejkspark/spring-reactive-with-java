package com.toby.ch2;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class PubSub3 {

    public static void main(String[] args) {

        // Publisher
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));

        // Operators
        Publisher<Integer> sumPub = sumPub(pub);

        // apply
        sumPub.subscribe(logSub());
    }

    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {

        return new Publisher<Integer>() {

            @Override
            public void subscribe(Subscriber<? super Integer> sub) {

                pub.subscribe(new DelegateSub(sub) {
                    int sum = 0;

                    @Override
                    public void onNext(Object o) {
                        sum += (Integer) o;
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(sum);
                        sub.onComplete();
                    }
                });
            }
        };
    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {

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
    }

    private static Publisher<Integer> iterPub(Iterable<Integer> iter) {
        return sub -> sub.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    iter.forEach(sub::onNext);
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
        });
    }
}
