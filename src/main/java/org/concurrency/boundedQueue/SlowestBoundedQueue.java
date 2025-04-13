package org.concurrency.boundedQueue;

import java.util.LinkedList;
import java.util.Queue;

public class SlowestBoundedQueue implements BoundedQueue {
    private final int size;
    private final Queue<Object> queue;

    public SlowestBoundedQueue(int size) {
        this.size = size;
        this.queue = new LinkedList<>();
    }

    public synchronized Object get() {
        try {
            while (queue.isEmpty()) {
                this.wait();
            }
        } catch (InterruptedException e) {
            System.out.println("Error while consuming");
        }
        Object o = queue.poll();
        this.notifyAll();
        return o;
    }

    public synchronized void put(Object o) {
        try {
            while (queue.size() >= size) {
                this.wait();
            }
        } catch (InterruptedException e) {
            System.out.println("Error while producing");
        }
        queue.add(o);
        this.notifyAll();
    }
}
