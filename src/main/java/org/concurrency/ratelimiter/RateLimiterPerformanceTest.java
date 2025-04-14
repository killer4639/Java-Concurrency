package org.concurrency.ratelimiter;

import org.concurrency.AutomatedTest;

public class RateLimiterPerformanceTest implements AutomatedTest {
    @Override
    public void run() {
        RateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 20);
        RateLimiterTest rateLimiterTest = new RateLimiterTest(rateLimiter, 100);
        rateLimiterTest.run();

        RateLimiter multiThreadedLimiter = new MultiThreadedTokenBucketRateLimiter(10, 20);
        RateLimiterTest rateLimiterTest1 = new RateLimiterTest(multiThreadedLimiter, 100);
        rateLimiterTest1.run();
    }
}
