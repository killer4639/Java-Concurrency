package org.concurrency;

import org.concurrency.ratelimiter.RateLimiterPerformanceTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest rateLimiterTest = new RateLimiterPerformanceTest();
        rateLimiterTest.run();
    }
}