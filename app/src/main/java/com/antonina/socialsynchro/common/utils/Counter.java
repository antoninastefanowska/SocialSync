package com.antonina.socialsynchro.common.utils;

public class Counter {
    private int value;

    public Counter() {
        value = 0;
    }

    public void increment() {
        value++;
    }

    public int getValue() {
        return value;
    }

    public int getNext() {
        return value++;
    }
}
