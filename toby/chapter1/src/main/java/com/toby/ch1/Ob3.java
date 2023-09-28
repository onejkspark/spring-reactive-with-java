package com.toby.ch1;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ob3 {
    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i); // push
                // int i = it.next(); // pull
            }
        }
    }


    // event source
    // data -> target is observable
    public static void main(String[] args) {

        // Get Data
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + ", arg : " + arg);
            }
        };

        // Push Data
        IntObservable io = new IntObservable();
        // sub
        io.addObserver(ob);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " Exit");
        es.shutdown();
    }
}
