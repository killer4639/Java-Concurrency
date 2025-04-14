package org.concurrency.delayedExecutor;

import org.concurrency.AutomatedTest;

import java.util.concurrent.TimeUnit;

public class DelayedExecutorTest implements AutomatedTest {
    @Override
    public void run() {
        DelayedExecutor delayedExecutor = new DelayedExecutor();
        long start = System.currentTimeMillis();
        delayedExecutor.submitTask(() -> {
            System.out.println("Thread 1: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        }, TimeUnit.SECONDS.toMillis(2));
        delayedExecutor.submitTask(() -> {
            System.out.println("Thread 2: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        }, TimeUnit.SECONDS.toMillis(5));
        delayedExecutor.submitTask(() -> {
            System.out.println("Thread 3: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            } catch (InterruptedException e) {
                System.out.println("Task interrupted");
            }
        }, TimeUnit.SECONDS.toMillis(1));
        delayedExecutor.submitTask(() -> {
            System.out.println("Thread 4: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        }, TimeUnit.SECONDS.toMillis(0));
        delayedExecutor.submitTask(() -> {
            System.out.println("Thread 5: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
        }, TimeUnit.SECONDS.toMillis(10));

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
        } catch (InterruptedException e) {
            // Ignore
        }

        delayedExecutor.shutdown();
    }
}
