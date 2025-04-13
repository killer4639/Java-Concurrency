package org.concurrency.boundedQueue;


/**
 * 1. Thread safe
 * 2. If the queue is empty, then the consumer thread should wait until there is any object in the queue.
 * 3. If the queue is full, then the producer thread should wait until there is some space in the queue.
 * 4. Consumer thread should get notified when there is an element in the queue, and it was waiting.
 * 5. Producer thread should get notified when there is space in the queue, and it was waiting.
 */

public interface BoundedQueue {
    public Object get();

    public void put(Object o);
}
