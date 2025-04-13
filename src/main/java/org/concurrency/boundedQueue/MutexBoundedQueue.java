package org.concurrency.boundedQueue;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Does not use synchronized keyword
 */
public class MutexBoundedQueue extends BoundedQueue {
    private final Queue<Object> queue;
    private final Lock lock;

    public MutexBoundedQueue(int size) {
        super(size);
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public Object get() {
        lock.lock();
        while (queue.isEmpty()) {
            lock.unlock();
            lock.lock();
        }

        Object obj = queue.poll();
        lock.unlock();
        return obj;
    }

    @Override
    public void put(Object o) {
        lock.lock();
        while (queue.size() >= this.getSize()) {
            lock.unlock();
            lock.lock();
        }

        queue.add(o);
        lock.unlock();
    }
}
