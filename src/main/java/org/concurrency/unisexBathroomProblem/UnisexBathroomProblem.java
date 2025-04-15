package org.concurrency.unisexBathroomProblem;

/**
 * 1. There cannot be men and women in the bathroom at the same time.
 * 2. There should never be more than three employees in the bathroom
 * simultaneously.
 */
public class UnisexBathroomProblem {
    private int counter = 0;
    private boolean male = false;

    public void maleUseBathroom() {
        synchronized (this) {
            while (!((male && counter < 3) || (!male && counter == 0))) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            male = true;
            counter++;
        }

        useBathroom();

        synchronized (this) {
            counter--;
            notifyAll();
        }
    }

    public void femaleUseBathroom() {
        synchronized (this) {
            while (!((!male && counter < 3) || (male && counter == 0))) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            male = false;
            counter++;
        }

        useBathroom();

        synchronized (this) {
            counter--;
            notifyAll();
        }
    }

    private void useBathroom() {
        String name = male ? "Male" : "Female";
        System.out.println("\n" + name + " using bathroom. Current employees in bathroom = " + counter);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupting while using");
        }
        System.out.println("\n" + name + " done using bathroom " + System.currentTimeMillis());
    }
}
