package org.concurrency.ratelimiter;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RateLimiterTest {
    private final RateLimiter rateLimiter;
    private final int readerCount;
    private final Random random = new Random();
    private final Runnable reader;
    private final long startedTime = System.currentTimeMillis();

    public RateLimiterTest(RateLimiter rateLimiter, int readerCount) {
        this.rateLimiter = rateLimiter;
        this.readerCount = readerCount;

        reader = () -> {
            while (true) {
                long randomWait = random.nextLong(TimeUnit.SECONDS.toMillis(1));
                try {
                    Thread.sleep(randomWait);
                    this.rateLimiter.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Reader thread interrupted");
                    break;
                }
                System.out.println("Acquired token at time: " + (System.currentTimeMillis() - startedTime) / TimeUnit.SECONDS.toMillis(1));
            }
        };
    }

    public void run() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < readerCount; i++) {
            Thread thread = new Thread(reader);
            thread.start();
            threads.add(thread);
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(20));
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }


        for (int i = 0; i < readerCount; i++) {
            threads.get(i).interrupt();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted while joining");
            }
        }
    }
}
