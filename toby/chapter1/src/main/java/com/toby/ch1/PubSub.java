package com.toby.ch1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Flow;

public class PubSub {

    public static void main(String[] args) {

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);

        Flow.Publisher p = (Flow.Subscriber subscriber) -> {

            Iterator<Integer> iterator = itr.iterator();

            subscriber.onSubscribe(new Flow.Subscription() {
                @Override
                public void request(long n) {
                    try {
                        while (n-- > 0) {
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
                System.out.println("onNext ==> " + item);
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

        p.subscribe(s);
    }
}
