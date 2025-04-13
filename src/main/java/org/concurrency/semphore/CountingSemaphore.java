package org.concurrency.semphore;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is similar to Semaphore in Java, but it has upper bound on the maximum number of permits.
 * It blocks if it tries to increase the number of permits more than the capacity.
 */
public class CountingSemaphore {

    private final int size;
    private volatile int permits;

    public CountingSemaphore(int permits) {
        this.size = permits;
        this.permits = permits;
    }

    public void acquire() {
        synchronized (this) {
            while (permits <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted acquire counting semaphore");
                }
            }
            permits--;
            this.notifyAll();
        }
    }

    public void release() {
        synchronized (this) {
            while (permits >= size) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted acquire counting semaphore");
                }
            }
            permits++;
            this.notifyAll();
        }
    }
}
