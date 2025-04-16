package org.concurrency.barrier;

public class Barrier {
    private final int count;
    private int released;
    private int counter;

    public Barrier(int count) {
        this.count = count;
    }

    public synchronized void await() {
        while (!((released == 0) || (counter < count))) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("Barrier interrupted");
            }
        }

        counter++;
        notifyAll();
        while ((counter < count) && (released == 0)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("Barrier interrupted");
            }
        }

        System.out.println("Barrier released");

        counter = 0;
        released++;
        released %= count;
        notifyAll();
    }
}
