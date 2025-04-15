package org.concurrency;

/**
 * A lock which lets multiple readers read at the same time, but only one writer write at a time.
 */
public class ReadWriteLock {
    private int readers;
    private boolean writeLock;

    public ReadWriteLock() {
        this.writeLock = false;
        this.readers = 0;
    }

    public synchronized void acquireReadLock() throws InterruptedException {
        while (writeLock) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new InterruptedException();
            }
        }
        readers++;
    }

    public synchronized void releaseReadLock() {
        readers--;
        notifyAll();
    }

    public synchronized void acquireWriteLock() throws InterruptedException {
        while (writeLock || readers > 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new InterruptedException();
            }
        }
        writeLock = true;
    }

    public synchronized void releaseWriteLock() {
        writeLock = false;
        this.notify();
    }
}