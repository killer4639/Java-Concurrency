package org.concurrency;

import org.concurrency.delayedExecutor.DelayedExecutorTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest rateLimiterTest = new DelayedExecutorTest();
        rateLimiterTest.run();
    }
}