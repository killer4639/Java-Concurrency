package org.concurrency;

import org.concurrency.boundedQueue.*;

public class Main {
    public static void main(String[] args) {
        BoundedQueue synchronizedBoundedQueue = new SynchronizedBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest1 = new BoundedQueuePerformanceTest(50, 250, synchronizedBoundedQueue);
        long time1 = queuePerformanceTest1.startTimeTakenTest(1000000);
        System.out.println("Total time taken by Synchronized queue: " + time1);

        BoundedQueue mutexQueue = new MutexBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest2 = new BoundedQueuePerformanceTest(50, 250, mutexQueue);
        long time2 = queuePerformanceTest2.startTimeTakenTest(1000000);
        System.out.println("Total time taken by mutex queue: " + time2);

        BoundedQueue semaphoreQueue = new SemaphoreBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest3 = new BoundedQueuePerformanceTest(50, 250, semaphoreQueue);
        long time3 = queuePerformanceTest3.startTimeTakenTest(1000000);
        System.out.println("Total time taken by semaphore queue: " + time3);
    }
}