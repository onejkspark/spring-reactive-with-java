package com.toby.ch3;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx1 {

    public static void main(String[] args) {

        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(
                    new Subscription() {
                        int no = 0;

                        boolean isCancel = false;

                        @Override
                        public void request(long n) {

                            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

                            exec.scheduleAtFixedRate(() -> {

                                if (isCancel) {
                                    exec.shutdown();
                                    return;
                                }

                                sub.onNext(no++);

                            }, 0, 300, TimeUnit.MICROSECONDS);

                        }

                        @Override
                        public void cancel() {
                            isCancel = true;
                        }
                    }
            );
        };

        Publisher<Integer> takePub = sub -> {

            pub.subscribe(
                    new Subscriber<Integer>() {

                        int count = 0;

                        Subscription subc;

                        @Override
                        public void onSubscribe(Subscription s) {
                            subc = s;
                            sub.onSubscribe(subc);
                        }

                        @Override
                        public void onNext(Integer integer) {
                            sub.onNext(integer);
                            if (++count >= 10) {
                                subc.cancel();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            sub.onError(t);
                        }

                        @Override
                        public void onComplete() {
                            sub.onComplete();
                        }
                    }
            );

        };


        takePub.subscribe(
                new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("onSubscribe");
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
                }
        );

    }
}
