package com.toby.ch1;

import java.util.Iterator;

public class Ob {


    public static void main(String[] args) {

        // cold code
        Iterable<Integer> iterable = () -> new Iterator<>() {
            int start = 0;
            final static int end = 10;

            @Override
            public boolean hasNext() {
                return start < end;
            }

            @Override
            public Integer next() {
                return ++start;
            }
        };

        for (Integer i : iterable) {
            System.out.println(i);
        }

    }
}
