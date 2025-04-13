package org.concurrency.boundedQueue;

import org.concurrency.AutomatedTest;

public class PerformanceTest implements AutomatedTest {
    @Override
    public void run() {
        BoundedQueue synchronizedBoundedQueue = new SynchronizedBoundedQueue(10);
        BoundedQueueTest queuePerformanceTest1 = new BoundedQueueTest(50, 250, synchronizedBoundedQueue);
        long time1 = queuePerformanceTest1.startTimeTakenTest(1000000);
        System.out.println("Total time taken by Synchronized queue: " + time1);

        BoundedQueue mutexQueue = new MutexBoundedQueue(10);
        BoundedQueueTest queuePerformanceTest2 = new BoundedQueueTest(50, 250, mutexQueue);
        long time2 = queuePerformanceTest2.startTimeTakenTest(1000000);
        System.out.println("Total time taken by mutex queue: " + time2);

        BoundedQueue semaphoreQueue = new SemaphoreBoundedQueue(10);
        BoundedQueueTest queuePerformanceTest3 = new BoundedQueueTest(50, 250, semaphoreQueue);
        long time3 = queuePerformanceTest3.startTimeTakenTest(1000000);
        System.out.println("Total time taken by semaphore queue: " + time3);
    }
}
