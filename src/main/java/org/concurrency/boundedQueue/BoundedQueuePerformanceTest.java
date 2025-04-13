package org.concurrency.boundedQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This performs a performance test on the queue that is provided. with the required number of reader and
 * writer threads
 * Creates the exact number of events that was given.
 * The provided number of reader threads keeps reading.
 * The provided number of writer threads keeps writing.
 * In the end, we can test:
 * -> How much time it took to create a particular number of events and consume them.
 */


/**
 * Issues
 * 1. The counter is not stopping at exactly the number of events I want it to create
 * Solution: Fixed it with CountDownLatch
 */
public class BoundedQueuePerformanceTest {
    private final int readerThreads;
    private final int writerThreads;
    private final BoundedQueue queue;
    private final CountDownLatch latch;

    public BoundedQueuePerformanceTest(int readerThreads, int writerThreads, BoundedQueue queue) {
        this.readerThreads = readerThreads;
        this.writerThreads = writerThreads;
        this.queue = queue;
        this.latch = new CountDownLatch(writerThreads);
    }

    public long startTimeTakenTest(long noOfObjects) {
        long start = System.currentTimeMillis();

        Queue<Thread> threads = startTest(noOfObjects);

        while (!threads.isEmpty()) {
            Thread thread = threads.poll();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Error while joining");
            }
        }

        return System.currentTimeMillis() - start;
    }

    private Queue<Thread> startTest(long noOfObjects) {
        AtomicInteger writerCounter = new AtomicInteger();
        Runnable writerThread = () -> {
            while (writerCounter.incrementAndGet() <= (noOfObjects)) {
                TestEvent testEvent = new TestEvent();
                this.queue.put(testEvent);
            }
            latch.countDown();
        };

        AtomicInteger readerCounter = new AtomicInteger();
        Runnable readerThread = () -> {
            while (true) {
                TestEvent event = (TestEvent) this.queue.get();
                if (!event.isDeadEvent) {
                    readerCounter.incrementAndGet();
                }
                if (event.isDeadEvent) {
                    System.out.println("Dead event detected with reader counter: " + readerCounter.get());
                    break;
                }
            }
        };

        Runnable putDeadEventsRunnable = () -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                System.out.println("Latch interrupted");
            }
            for (int i = 0; i < readerThreads; i++) {
                this.queue.put(new TestEvent(true));
            }
        };

        Queue<Thread> threads = new LinkedList<>();
        for (int i = 0; i < writerThreads; i++) {
            Thread thread = new Thread(writerThread);
            threads.add(thread);
            thread.start();
        }

        for (int i = 0; i < readerThreads; i++) {
            Thread thread = new Thread(readerThread);
            threads.add(thread);
            thread.start();
        }

        Thread putDeadEventsThread = new Thread(putDeadEventsRunnable);
        putDeadEventsThread.start();
        threads.add(putDeadEventsThread);

        return threads;
    }

    private class TestEvent {
        private long time;
        private boolean isDeadEvent;

        public TestEvent() {
            this.time = System.currentTimeMillis();
        }

        public TestEvent(boolean isDeadEvent) {
            this.time = System.currentTimeMillis();
            this.isDeadEvent = isDeadEvent;
        }

        public long getTime() {
            return this.time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public boolean isDeadEvent() {
            return isDeadEvent;
        }

        public void setDeadEvent(boolean deadEvent) {
            isDeadEvent = deadEvent;
        }
    }
}
