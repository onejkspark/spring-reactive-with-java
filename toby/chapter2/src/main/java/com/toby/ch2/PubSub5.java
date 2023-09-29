package com.toby.ch2;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class PubSub5 {

    public static void main(String[] args) {

        // Publisher
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));

        // Operators
        Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);

        // apply
        mapPub.subscribe(logSub());
    }

    private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                pub.subscribe(
                        new DelegateSub<T, R>(sub) {
                            @Override
                            public void onNext(T o) {
                                sub.onNext(f.apply(o));
                            }
                        }
                );
            }
        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {

            @Override
            public void onSubscribe(Subscription s) {
                // 무제한 데이터 발생
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T integer) {
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
