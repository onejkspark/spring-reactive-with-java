package com.toby.ch1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.*;

public class PubSub2 {

    public static void main(String[] args) throws InterruptedException {

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
        ExecutorService es = Executors.newCachedThreadPool();

        Flow.Publisher p = (Flow.Subscriber subscriber) -> {

            Iterator<Integer> iterator = itr.iterator();

            subscriber.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(long n) {
                    es.execute(() -> {
                        int i = 0;
                        try {
                            while (i++ < n) {
                                if (iterator.hasNext()) {
                                    subscriber.onNext(iterator.next());
                                } else {
                                    subscriber.onComplete();
                                    break;
                                }
                            }
                        } catch (RuntimeException e) {
                            subscriber.onError(e);
                        }
                    });


                }

                @Override
                public void cancel() {

                }
            });

        };

        Flow.Subscriber<Integer> s = new Flow.Subscriber<Integer>() {

            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println("onSubscribe ==>");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + ", onNext ==> " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError ==> " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete ==>");
            }
        };

        System.out.println(Thread.currentThread().getName() + ", End");

        p.subscribe(s);
        es.awaitTermination(10, TimeUnit.SECONDS);
        es.shutdown();
    }
}
