package org.concurrency.ratelimiter;

import org.concurrency.AutomatedTest;

public class RateLimiterPerformanceTest implements AutomatedTest {
    @Override
    public void run() {
        RateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 20);
        RateLimiterTest rateLimiterTest = new RateLimiterTest(rateLimiter, 100);
        rateLimiterTest.run();
    }
}
