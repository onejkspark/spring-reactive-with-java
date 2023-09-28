package com.toby.ch1;

import java.util.Observable;
import java.util.Observer;

public class Ob2 {
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
                System.out.println(arg);
            }
        };

        // Push Data
        IntObservable io = new IntObservable();
        // sub
        io.addObserver(ob);
        // run
        io.run();
    }
}
