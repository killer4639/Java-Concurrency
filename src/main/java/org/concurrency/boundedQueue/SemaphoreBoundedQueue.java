package org.concurrency.boundedQueue;

import org.concurrency.semphore.CountingSemaphore;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * This bounded queue uses semaphores
 */
public class SemaphoreBoundedQueue extends BoundedQueue {

    private final CountingSemaphore semaphore;
    private final Queue<Object> queue;

    public SemaphoreBoundedQueue(int size) {
        super(size);
        queue = new LinkedList<>();
        semaphore = new CountingSemaphore(size);
    }

    @Override
    public Object get() {
        synchronized (semaphore) {
            semaphore.release();
            return queue.poll();
        }
    }

    @Override
    public void put(Object o) {
        synchronized (semaphore) {
            semaphore.acquire();
            queue.add(o);
        }
    }
}
