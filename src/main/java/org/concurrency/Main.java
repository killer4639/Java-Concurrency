package org.concurrency;

import org.concurrency.boundedQueue.BoundedQueue;
import org.concurrency.boundedQueue.BoundedQueuePerformanceTest;
import org.concurrency.boundedQueue.SlowestBoundedQueue;

public class Main {
    public static void main(String[] args) {
        BoundedQueue boundedQueue1 = new SlowestBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest1 = new BoundedQueuePerformanceTest(500, 250, boundedQueue1);
        long time1 = queuePerformanceTest1.startTimeTakenTest(1000000);

        BoundedQueue boundedQueue2 = new SlowestBoundedQueue(10);
        BoundedQueuePerformanceTest queuePerformanceTest2 = new BoundedQueuePerformanceTest(50, 250, boundedQueue2);
        long time2 = queuePerformanceTest2.startTimeTakenTest(1000000);

        System.out.println("Time difference: " + (time1 - time2));
    }
}