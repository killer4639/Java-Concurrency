package org.concurrency;

import org.concurrency.boundedQueue.BoundedQueue;
import org.concurrency.boundedQueue.BoundedQueuePerformanceTest;
import org.concurrency.boundedQueue.MutexBoundedQueue;
import org.concurrency.boundedQueue.SynchronizedBoundedQueue;

public class Main {
    public static void main(String[] args) {
        BoundedQueue boundedQueue1 = new SynchronizedBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest1 = new BoundedQueuePerformanceTest(500, 250, boundedQueue1);
        long time1 = queuePerformanceTest1.startTimeTakenTest(1000000);

        BoundedQueue boundedQueue2 = new SynchronizedBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest2 = new BoundedQueuePerformanceTest(50, 250, boundedQueue2);
        long time2 = queuePerformanceTest2.startTimeTakenTest(1000000);

        BoundedQueue mutexQueue = new MutexBoundedQueue(1000);
        BoundedQueuePerformanceTest queuePerformanceTest3 = new BoundedQueuePerformanceTest(50, 250, mutexQueue);
        long time3 = queuePerformanceTest3.startTimeTakenTest(1000000);
        System.out.println("Total time taken: " + time3);
        System.out.println("Time difference: " + (time2 - time3));
    }
}